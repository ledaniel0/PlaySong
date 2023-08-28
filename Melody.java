package Project2;

/**
 * Name: Daniel Le
 * Section: CS143 7707
 * <p>
 * <p>
 * Description:
 * The Melody class represents a collection of musical notes that can be played sequentially.
 * It contains methods for calculating the total duration of the melody, changing the tempo of the
 * melody, reversing the order of the notes in the melody, appending another melody to the current
 * melody, and playing the melody. The melody is stored as a queue of Note objects, where each Note
 * object represents a single musical note with a duration and pitch. The class also includes a
 * private method for identifying and extracting repeated sections within the melody, which can be
 * useful for playing those sections repeatedly. The class includes error checking to ensure that
 * invalid arguments are not passed to methods.
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Melody {
    private Queue<Note> song;

    public Melody(Queue<Note> song) {
        if (song.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.song = new LinkedList<>(song);
    }

    /**
     * The getTotalDuration method calculates the duration of the whole song.
     * <p>
     * precondition: The getTotalDuration method does not have any specific preconditions.
     * However, it assumes that the song queue contains Note objects with a valid duration
     * value. If the song queue is empty, the method returns 0.0.
     *
     * @return
     */
    public double getTotalDuration() {
        double noteDuration = 0.0;
        double songDuration = 0.0;
        if (!song.isEmpty()) {
            noteDuration = song.remove().getDuration();
            songDuration = noteDuration + getTotalDuration();
            return songDuration;
        }
        return 0.0;
    }

    /**
     * The toString method returns a big string with information of the notes.
     * It prints one note per line.
     * precondition: The toString() method does not have a specific precondition,
     * but it is typically called on a valid object that has been initialized
     * with valid data. If the object is null or has not been properly initialized,
     * calling toString() may result in a NullPointerException.
     *
     * @return
     */
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < song.size(); i++) {
            result += song.peek().toString() + "\n";
            song.add(song.remove());
        }
        return result;
    }

    /**
     * The changeTempo method takes in the tempo percentage and then
     * changes the tempo of each note.
     * <p>
     * precondition: The precondition for the changeTempo method is
     * that the tempo argument must be a positive number. If the
     * tempo argument is less than or equal to zero, then an
     * IllegalArgumentException is thrown.
     *
     * @param tempo
     */
    public void changeTempo(double tempo) {
        if (tempo <= 0) {
            throw new IllegalArgumentException();
        }
        int size = song.size();
        for (int i = 0; i < size; i++) {
            song.peek().setDuration(song.peek().getDuration() * tempo);
            song.add(song.remove());
        }
    }

    /**
     * The reverse method reverses the entire song.
     * <p>
     * precondition: The reverse() method does not
     * have any explicit preconditions, but it assumes
     * that the song queue is not null. If the song
     * queue is null, a NullPointerException will be
     * thrown when calling song.isEmpty().
     */
    public void reverse() {
        Stack<Note> stack = new Stack<>();
        while (!song.isEmpty()) {
            Note note = song.remove();
            stack.push(note);
        }
        while (!stack.isEmpty()) {
            song.add(stack.pop());
        }
    }

    /**
     * The append method adds the other argument to the back
     * of the song queue.
     * <p>
     * precondition: The precondition for the append method is that
     * the other Melody object passed as a parameter must not be null.
     *
     * @param other
     */
    public void append(Melody other) {
        Queue<Note> temp = new LinkedList<>(other.song);
        song.addAll(temp);
    }

    /**
     * The play method plays the song. If a section of the song is a repeat
     * it plays the repeated section twice.
     * <p>
     * precondition: The precondition for the play() method is that the song
     * queue should not be empty and should only contain objects of the Note class.
     */
    public void play() {
        Queue<Note> tempQueue = new LinkedList<>(getRepeatedSection());
        Note currentNote = song.peek();
        boolean firstTrue = false;
        int x = 0;
        int songSize = song.size();
        int restOfSong = 0;
        while (!currentNote.isRepeat()) {
            song.add(song.remove());
            currentNote = song.peek();
            x++;
            if (x >= songSize) {
                break;
            }
        }
        if (currentNote.isRepeat()) {
            firstTrue = true;
        }
        restOfSong = songSize - x;
        for (int j = 0; j < restOfSong; j++) {
            song.add(song.remove());
        }

        for (int i = 0; i < song.size(); i++) {
            if (song.peek().isRepeat() && firstTrue) {
                while (!tempQueue.isEmpty()) {
                    tempQueue.remove().play();
                }
                song.peek().play();
                song.add(song.remove());
            } else {
                song.peek().play();
                song.add(song.remove());
            }
        }
    }

    /**
     * The getRepeatedSection method is a private method that is a helper method to other methods.
     * It returns a new Queue with just the repeated section inside. This means that there
     * is no precondition.
     *
     * @return repeatedSection
     */
    private Queue<Note> getRepeatedSection() {
        Queue<Note> repeatedSection = new LinkedList<>();
        Note currentNote = song.peek();
        boolean firstTrue = false;
        int i = 0;
        int restOfSong = 0;
        while (!currentNote.isRepeat()) {
            song.add(song.remove());
            currentNote = song.peek();
            i++;
            if (i >= song.size()) {
                break;
            }
        }
        if (currentNote.isRepeat()) {
            firstTrue = true;
            repeatedSection.add(currentNote);
            song.add(song.remove());
            currentNote = song.peek();
            i++;
        }

        while (currentNote != null && !currentNote.isRepeat() && firstTrue) {
            repeatedSection.add(currentNote);
            song.add(song.remove());
            currentNote = song.peek();
            i++;
            if (i >= song.size()) {
                break;
            }
        }

        if (currentNote.isRepeat() && firstTrue) {
            repeatedSection.add(currentNote);
        }

        restOfSong = song.size() - i;
        for (int j = 0; j < restOfSong; j++) {
            song.add(song.remove());
        }
        return repeatedSection;
    }
}
