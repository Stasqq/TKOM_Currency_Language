import inputReaders.CodeReader;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

public class CodeReaderTest {

    @Test
    public void singleCharTest() throws IOException {
        CodeReader codeReader = new CodeReader(new StringReader("a"));
        Assert.assertEquals(Character.valueOf('a'), codeReader.getCharacter());
    }

    @Test
    public void checkNextCharacterTest() throws IOException {
        CodeReader codeReader = new CodeReader(new StringReader("ab"));
        Assert.assertEquals(Character.valueOf('a'), codeReader.getCharacter());
        Assert.assertEquals(1 ,codeReader.getCharacterPosition());
        Assert.assertEquals(Character.valueOf('b'), codeReader.checkNextCharacter());
        Assert.assertEquals(1 ,codeReader.getCharacterPosition());
    }

    @Test
    public void undoTest() throws IOException {
        CodeReader codeReader = new CodeReader(new StringReader("ab"));
        Assert.assertEquals(Character.valueOf('a'), codeReader.getCharacter());
        Assert.assertEquals(Character.valueOf('b'), codeReader.getCharacter());
        Assert.assertEquals(2 ,codeReader.getCharacterPosition());
        codeReader.undo();
        Assert.assertEquals(1 ,codeReader.getCharacterPosition());
        Assert.assertEquals(Character.valueOf('b'), codeReader.getCharacter());
    }

    @Test
    public void EOFTest() throws IOException {
        CodeReader codeReader = new CodeReader(new StringReader("ab"));
        Assert.assertEquals(Character.valueOf('a'), codeReader.getCharacter());
        Assert.assertEquals(Character.valueOf('b'), codeReader.getCharacter());
        Assert.assertEquals(Character.valueOf(CodeReader.EOF_CHAR), codeReader.getCharacter());
    }
}
