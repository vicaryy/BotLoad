package org.vicary.command;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TerminalCommand {
    private final String removeFile = "rm";
    private final String renameCommand = "mv";
}
