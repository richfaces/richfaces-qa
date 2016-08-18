package org.richfaces.tests.metamer.ftest.extension.creaper;

import org.apache.commons.lang.StringUtils;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.offline.OfflineCommand;
import org.wildfly.extras.creaper.core.offline.OfflineCommandContext;
import org.wildfly.extras.creaper.core.online.CliException;
import org.wildfly.extras.creaper.core.online.OnlineCommand;
import org.wildfly.extras.creaper.core.online.OnlineCommandContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command for patch application. OfflineCommand only for standalone mode!
 */
public final class ApplyPatch implements OnlineCommand, OfflineCommand {
    private final String patchPath;
    private final Boolean overrideAll;
    private final Boolean overrideModules;
    private final List<String> overridePaths;
    private final List<String> preservePaths;

    private ApplyPatch(Builder builder) {
        this.patchPath = builder.patchPath;
        this.overrideAll = builder.overrideAll;
        this.overrideModules = builder.overrideModules;
        this.overridePaths = builder.overridePaths;
        this.preservePaths = builder.preservePaths;
    }

    @Override
    public void apply(OnlineCommandContext ctx) throws CliException, CommandFailedException, IOException {
        StringBuilder cmd = buildCliCommandBase();
        if (ctx.options.isDomain) {
            cmd.append(" --host=");
            cmd.append(ctx.options.defaultHost);
        }
        ctx.client.executeCli(cmd.toString());
    }

    @Override
    public String toString() {
        return "ApplyPatch " + patchPath;
    }

    @Override
    public void apply(OfflineCommandContext ctx) throws Exception {
        executeCliOffline(buildCliCommandBase().toString(), ctx);
    }

    private StringBuilder buildCliCommandBase() {
        StringBuilder cmd = new StringBuilder("patch apply " + patchPath);
        if (overrideAll != null && overrideAll) {
            cmd.append(" --override-all");
        }
        if (overrideModules != null && overrideModules) {
            cmd.append(" --override-modules");
        }
        if (overridePaths != null && !overridePaths.isEmpty()) {
            cmd.append(" --override=");
            cmd.append(PatchingConversions.flatten(overridePaths));
        }
        if (preservePaths != null && !preservePaths.isEmpty()) {
            cmd.append(" --preserve=");
            cmd.append(PatchingConversions.flatten(preservePaths));
        }
        return cmd;
    }

    public static final class Builder {
        private final String patchPath;
        private Boolean overrideAll;
        private Boolean overrideModules;
        private List<String> overridePaths;
        private List<String> preservePaths;

        /**
         * @param patchPath path to patch file
         */
        public Builder(String patchPath) {
            this.patchPath = patchPath;
        }

        /**
         * @param patchFile patch file
         */
        public Builder(File patchFile) {
            this.patchPath = patchFile.getAbsolutePath();
        }

        /**
         * Sets whether all conflicts should be automatically resolved by overriding the data using those from patch.
         */
        public Builder overrideAll(boolean overrideAll) {
            this.overrideAll = overrideAll;
            return this;
        }

        /**
         * Sets whether modules shall be overridden when there is conflict in the module.
         */
        public Builder overrideModules(boolean overrideModules) {
            this.overrideModules = overrideModules;
            return this;
        }

        /**
         * Adds specified paths to the list of paths which shall be overridden.
         * For more details, see the {@code --override} option of the {@code patch apply} CLI command.
         */
        public Builder overridePaths(String... pathsToOverride) {
            if (this.overridePaths == null && pathsToOverride != null) {
                this.overridePaths = new ArrayList<String>();
            }
            if (pathsToOverride != null) {
                this.overridePaths.addAll(Arrays.asList(pathsToOverride));
            }
            return this;

        }

        /**
         * Adds paths to list of paths which should be preserved.
         * For more details, see the {@code --preserve} option of the {@code patch apply} CLI command.
         */
        public Builder preservePaths(String... pathsToPreserve) {
            if (this.preservePaths == null && pathsToPreserve != null) {
                this.preservePaths = new ArrayList<String>();
            }
            if (pathsToPreserve != null) {
                this.preservePaths.addAll(Arrays.asList(pathsToPreserve));
            }
            return this;
        }

        public ApplyPatch build() {
            return new ApplyPatch(this);
        }
    }

    private boolean isOnWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private void executeCliOffline(String cliCommand, OfflineCommandContext ctx) throws IOException, InterruptedException {
        String configDirPath = ctx.client.options().configurationDirectory().getAbsolutePath();

        List<String> rootDirPathSteps = new ArrayList<String>(Arrays.asList(configDirPath.split(File.separator)));

        String rootDirPath = StringUtils.join(rootDirPathSteps.subList(0, rootDirPathSteps.size() - 2), File.separator);

        System.out.println("Root path = '" + rootDirPath + "'.");

        StringBuilder command = new StringBuilder()
                .append(rootDirPath)
                .append(File.separator)
                .append("bin")
                .append(File.separator)
                .append("jboss-cli")
                .append(isOnWindows() ? ".bat" : ".sh");

        Process p = Runtime.getRuntime().exec("chmod +x " + command.toString());
        p.waitFor();

        command.append(" command=\"")
            .append(cliCommand)
            .append("\"");

        Runtime.getRuntime().exec(command.toString());
    }
}
