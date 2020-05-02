package uk.co.automatictester.lightning.standalone.cli;

import org.mockito.Mockito;
import org.springframework.boot.ApplicationArguments;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class CliParamsTest {

    @Test
    public void testGetFileFromOptionValue() {
        String option = "xml";
        String value = "pom.xml";

        ApplicationArguments args = Mockito.mock(ApplicationArguments.class);
        List<String> values = Collections.singletonList(value);

        Mockito.when(args.containsOption(option)).thenReturn(true);
        Mockito.when(args.getOptionValues(option)).thenReturn(values);

        File expectedFile = new File(value);
        File actualFile = new CliParams(args).getFileFromOptionValue(option);
        assertThat(actualFile).isEqualTo(expectedFile);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testGetFileFromOptionValue_notProvided() {
        ApplicationArguments args = Mockito.mock(ApplicationArguments.class);

        Mockito.when(args.containsOption("xml")).thenReturn(false);

        new CliParams(args).getFileFromOptionValue("xml");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testGetFileFromOptionValue_nonexistent() {
        ApplicationArguments args = Mockito.mock(ApplicationArguments.class);
        List<String> values = Collections.singletonList("nonexistent");

        Mockito.when(args.containsOption("xml")).thenReturn(false);
        Mockito.when(args.getOptionValues("xml")).thenReturn(values);

        new CliParams(args).getFileFromOptionValue("xml");
    }

    @Test
    public void testIsPerfmonCsvFileProvided_true() {
        ApplicationArguments args = Mockito.mock(ApplicationArguments.class);
        List<String> values = Collections.singletonList("pom.xml");

        Mockito.when(args.containsOption("perfmon-csv")).thenReturn(true);
        Mockito.when(args.getOptionValues("perfmon-csv")).thenReturn(values);

        boolean is = new CliParams(args).isPerfmonCsvFileProvided();
        assertThat(is).isEqualTo(true);
    }

    @Test
    public void testIsPerfmonCsvFileProvided_false() {
        ApplicationArguments args = Mockito.mock(ApplicationArguments.class);

        Mockito.when(args.containsOption("perfmon-csv")).thenReturn(false);

        boolean is = new CliParams(args).isPerfmonCsvFileProvided();
        assertThat(is).isEqualTo(false);
    }

    @Test
    public void testGetParsedCommand_report() {
        ApplicationArguments args = Mockito.mock(ApplicationArguments.class);
        List<String> values = Collections.singletonList("report");

        Mockito.when(args.getNonOptionArgs()).thenReturn(values);

        Optional<String> command = new CliParams(args).getParsedCommand();
        assertThat(command).isEqualTo(Optional.of("report"));
    }

    @Test
    public void testGetParsedCommand_verify() {
        ApplicationArguments args = Mockito.mock(ApplicationArguments.class);
        List<String> values = Collections.singletonList("verify");

        Mockito.when(args.getNonOptionArgs()).thenReturn(values);

        Optional<String> command = new CliParams(args).getParsedCommand();
        assertThat(command).isEqualTo(Optional.of("verify"));
    }

    @Test
    public void testGetParsedCommand_none() {
        ApplicationArguments args = Mockito.mock(ApplicationArguments.class);
        List<String> values = Collections.emptyList();

        Mockito.when(args.getNonOptionArgs()).thenReturn(values);

        Optional<String> command = new CliParams(args).getParsedCommand();
        assertThat(command).isEqualTo(Optional.empty());
    }

    @Test
    public void testGetParsedCommand_tooMany() {
        ApplicationArguments args = Mockito.mock(ApplicationArguments.class);
        List<String> values = Arrays.asList("report", "verify");

        Mockito.when(args.getNonOptionArgs()).thenReturn(values);

        Optional<String> command = new CliParams(args).getParsedCommand();
        assertThat(command).isEqualTo(Optional.empty());
    }

    @Test
    public void testIsHelpRequested_h() {
        ApplicationArguments args = Mockito.mock(ApplicationArguments.class);

        Mockito.when(args.containsOption("h")).thenReturn(true);
        Mockito.when(args.containsOption("help")).thenReturn(false);

        boolean is = new CliParams(args).isHelpRequested();
        assertThat(is).isEqualTo(true);
    }

    @Test
    public void testIsHelpRequested_help() {
        ApplicationArguments args = Mockito.mock(ApplicationArguments.class);

        Mockito.when(args.containsOption("h")).thenReturn(false);
        Mockito.when(args.containsOption("help")).thenReturn(true);

        boolean is = new CliParams(args).isHelpRequested();
        assertThat(is).isEqualTo(true);
    }

    @Test
    public void testIsHelpRequested_false() {
        ApplicationArguments args = Mockito.mock(ApplicationArguments.class);

        Mockito.when(args.containsOption("h")).thenReturn(false);
        Mockito.when(args.containsOption("help")).thenReturn(false);

        boolean is = new CliParams(args).isHelpRequested();
        assertThat(is).isEqualTo(false);
    }
}
