package pt.pchouse.pdf.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MetadataTest
{

    /**
     * Tests for the {@code equals} method of the {@code Metadata} class.
     * This ensures proper equality checks between different Metadata objects,
     * based on their attributes.
     */

    @Test
    void testEquals_SameObject() {
        Metadata metadata = new Metadata();
        metadata.setTitle("Title1");
        metadata.setAuthor("Author1");
        metadata.setSubject("Subject1");
        metadata.setKeywords("Keyword1");
        metadata.setApplication("Application1");
        metadata.setCreator("Creator1");
        metadata.setDisplayMetadataTitle(true);
        metadata.setAllowModifyAnnotation(true);
        metadata.setJavascript("javascript");
        metadata.setOwnerPassword("<PASSWORD>");
        metadata.setUserPassword("<PASSWORD>");
        metadata.setAllowPrinting(true);
        metadata.setAllowModifying(true);
        metadata.setAllowCopying(true);
        metadata.setAllowFillIn(true);
        metadata.setAllowScreenReaders(false);
        metadata.setAllowAssembly(true);
        metadata.setAllowDegradedPrinting(true);
        metadata.setAllowModifyAnnotation(true);

        assertEquals(metadata, metadata);
    }

    @Test
    void testEquals_EqualObjects() {
        Metadata metadata1 = new Metadata();
        metadata1.setTitle("Title1");
        metadata1.setAuthor("Author1");
        metadata1.setSubject("Subject1");
        metadata1.setKeywords("Keyword1");
        metadata1.setApplication("Application1");
        metadata1.setCreator("Creator1");
        metadata1.setDisplayMetadataTitle(true);
        metadata1.setAllowModifyAnnotation(true);
        metadata1.setJavascript("javascript");
        metadata1.setOwnerPassword("<PASSWORD>");
        metadata1.setUserPassword("<PASSWORD>");
        metadata1.setAllowPrinting(true);
        metadata1.setAllowModifying(true);
        metadata1.setAllowCopying(true);
        metadata1.setAllowFillIn(true);
        metadata1.setAllowScreenReaders(false);
        metadata1.setAllowAssembly(true);
        metadata1.setAllowDegradedPrinting(true);

        Metadata metadata2 = new Metadata();
        metadata2.setTitle("Title1");
        metadata2.setAuthor("Author1");
        metadata2.setSubject("Subject1");
        metadata2.setKeywords("Keyword1");
        metadata2.setApplication("Application1");
        metadata2.setCreator("Creator1");
        metadata2.setDisplayMetadataTitle(true);
        metadata2.setAllowModifyAnnotation(true);
        metadata2.setJavascript("javascript");
        metadata2.setOwnerPassword("<PASSWORD>");
        metadata2.setUserPassword("<PASSWORD>");
        metadata2.setAllowPrinting(true);
        metadata2.setAllowModifying(true);
        metadata2.setAllowCopying(true);
        metadata2.setAllowFillIn(true);
        metadata2.setAllowScreenReaders(false);
        metadata2.setAllowAssembly(true);
        metadata2.setAllowDegradedPrinting(true);

        assertEquals(metadata1, metadata2);
    }

    @Test
    void testEquals_DifferentTitle() {
        Metadata metadata1 = new Metadata();
        metadata1.setTitle("Title1");

        Metadata metadata2 = new Metadata();
        metadata2.setTitle("Title2");

        assertNotEquals(metadata1, metadata2);
    }

    @Test
    void testEquals_DifferentAuthor() {
        Metadata metadata1 = new Metadata();
        metadata1.setAuthor("Author1");

        Metadata metadata2 = new Metadata();
        metadata2.setAuthor("Author2");

        assertNotEquals(metadata1, metadata2);
    }

    @Test
    void testEquals_DifferentSubject() {
        Metadata metadata1 = new Metadata();
        metadata1.setSubject("Subject1");

        Metadata metadata2 = new Metadata();
        metadata2.setSubject("Subject2");

        assertNotEquals(metadata1, metadata2);
    }

    @Test
    void testEquals_DifferentKeywords() {
        Metadata metadata1 = new Metadata();
        metadata1.setKeywords("Keyword1");

        Metadata metadata2 = new Metadata();
        metadata2.setKeywords("Keyword2");

        assertNotEquals(metadata1, metadata2);
    }

    @Test
    void testEquals_DifferentApplication() {
        Metadata metadata1 = new Metadata();
        metadata1.setApplication("Application1");

        Metadata metadata2 = new Metadata();
        metadata2.setApplication("Application2");

        assertNotEquals(metadata1, metadata2);
    }

    @Test
    void testEquals_DifferentCreator() {
        Metadata metadata1 = new Metadata();
        metadata1.setCreator("Creator1");

        Metadata metadata2 = new Metadata();
        metadata2.setCreator("Creator2");

        assertNotEquals(metadata1, metadata2);
    }

    @Test
    void testEquals_DifferentDisplayMetadataTitle() {
        Metadata metadata1 = new Metadata();
        metadata1.setDisplayMetadataTitle(true);

        Metadata metadata2 = new Metadata();
        metadata2.setDisplayMetadataTitle(false);

        assertNotEquals(metadata1, metadata2);
    }
}