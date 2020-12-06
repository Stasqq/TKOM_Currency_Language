package inputReaders;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

public class CodeReader {

    private LineNumberReader reader;

    private Character character = Character.UNASSIGNED;
    private int characterPosition;

    private static final int BUFFER_SIZE = 4;

    public static final char EOF_CHAR = (char) -1;

    public CodeReader(Reader reader)
    {
        this.reader = new LineNumberReader(reader);
    }

    public Character checkNextCharacter() throws IOException
    {
        reader.mark(BUFFER_SIZE);
        character = (char) reader.read();
        reader.reset();
        return character;
    }

    public Character getCharacter() throws IOException
    {
        if(character == 'n')
            characterPosition = 0;

        reader.mark(BUFFER_SIZE);
        character = (char) reader.read();
        characterPosition++;

        return character;
    }

    public void undo() throws IOException
    {
        reader.reset();
        characterPosition--;
    }

    public int getReaderLineNumber()
    {
        return this.reader.getLineNumber() + 1;
    }

    public int getCharacterPosition()
    {
        return characterPosition;
    }
}
