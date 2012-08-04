/*
 * Copyright 2012 Brian Matthews
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.btmatthews.maven.plugins.crx.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;

import java.io.File;

import com.btmatthews.maven.plugins.crx.CRXArchiver;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Unit test the {@link CRXArchiver} in isolation.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.1.0
 */
public class TestArchiver {

    /**
     * The {@link CRXArchiver} used in unit tests.
     */
    private CRXArchiver archiver;

    /**
     * Temporary folder in which the generated .crx file will be created. JUnit will automatically dispose of this
     * temporary folder and its contents after the unit tests are completed.
     */
    @Rule
    public TemporaryFolder outputDirectory = new TemporaryFolder();

    /**
     * Initialise the unit tests.
     */
    @Before
    public void setUp() throws Exception {
        archiver = new CRXArchiver();
        archiver.setDestFile(outputDirectory.newFile("HelloWord-1.0.0-SNAPSHOT.crx"));
    }

    /**
     * Verify that a .crx file can be created without a pemPassword or classifier property.
     *
     * @throws Exception If there was an error executing the unit test.
     */
    @Test
    public void testArchiver() throws Exception {
        archiver.setPemFile(new File("target/test-classes/crxtest.pem"));
        archiver.addDirectory(new File("target/test-classes/HelloWorld"), null, null);
        archiver.createArchive();
    }

    /**
     * Verify that a .crx file can be created with a pemPassword but without a classifier property.
     *
     * @throws Exception If there was an error executing the unit test.
     */
    @Test
    public void testArchiverWithPasssword() throws Exception {
        archiver.setPemFile(new File("target/test-classes/crxtest1.pem"));
        archiver.setPemPassword("everclear");
        archiver.addDirectory(new File("target/test-classes/HelloWorld"), null, null);
        archiver.createArchive();
    }

    /**
     * Verify that an exception is raised when trying to sign a .crx file with a nonexistent PEM file.
     *
     * @throws Exception If there was an error executing the unit test.
     */
    @Test
    public void testArchiverWhenPEMFileDoesNotExist() throws Exception {
        try {
            archiver.setPemFile(new File("target/test-classes/crxtest2.pem"));
            archiver.setPemPassword("everclear");
            archiver.addDirectory(new File("target/test-classes/HelloWorld"), null, null);
            archiver.createArchive();
            fail();
        } catch (final ArchiverException e) {
            assertEquals("Could not load the public/private key from the PEM file", e.getMessage());
        }
    }

    /**
     * Verify that an exception is raised when trying to sign a .crx file with a corrupted PEM file.
     *
     * @throws Exception If there was an error executing the unit test.
     */
    @Test
    public void testArchiverWhenPEMFileIsCorrupted() throws Exception {
        try {
            archiver.setPemFile(new File("target/test-classes/crxtest3.pem"));
            archiver.setPemPassword("everclear");
            archiver.addDirectory(new File("target/test-classes/HelloWorld"), null, null);
            archiver.createArchive();
            fail();
        } catch (final ArchiverException e) {
            assertEquals("Could not load the public/private key from the PEM file", e.getMessage());
        }
    }
}
