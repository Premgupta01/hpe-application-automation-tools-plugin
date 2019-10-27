/*
 * Certain versions of software and/or documents ("Material") accessible here may contain branding from
 * Hewlett-Packard Company (now HP Inc.) and Hewlett Packard Enterprise Company.  As of September 1, 2017,
 * the Material is now offered by Micro Focus, a separately owned and operated company.  Any reference to the HP
 * and Hewlett Packard Enterprise/HPE marks is historical in nature, and the HP and Hewlett Packard Enterprise/HPE
 * marks are the property of their respective owners.
 * __________________________________________________________________
 * MIT License
 *
 * (c) Copyright 2012-2019 Micro Focus or one of its affiliates.
 *
 * The only warranties for products and services of Micro Focus and its affiliates
 * and licensors ("Micro Focus") are set forth in the express warranty statements
 * accompanying such products and services. Nothing herein should be construed as
 * constituting an additional warranty. Micro Focus shall not be liable for technical
 * or editorial errors or omissions contained herein.
 * The information contained herein is subject to change without notice.
 * ___________________________________________________________________
 */
package com.microfocus.application.automation.tools.octane.executor;

import com.hp.octane.integrations.uft.items.UftTestDiscoveryResult;
import com.microfocus.application.automation.tools.octane.configuration.SDKBasedLoggerProvider;
import hudson.remoting.VirtualChannel;
import jenkins.MasterToSlaveFileCallable;
import org.apache.logging.log4j.Logger;
import org.jenkinsci.remoting.RoleChecker;

import java.io.File;
import java.io.IOException;

/**
 * used to save final UFTTestDetection results in slave machine workspace
 */
public class UFTTestDetectionFinalResultSaverCallable extends MasterToSlaveFileCallable<String> {
    private UftTestDiscoveryResult results;
    private int buildNumber;

    private static final Logger logger = SDKBasedLoggerProvider.getLogger(UFTTestDetectionFinalResultSaverCallable.class);

    public UFTTestDetectionFinalResultSaverCallable(UftTestDiscoveryResult results, int buildNumber) {
        this.results = results;
        this.buildNumber = buildNumber;
    }

    @Override
    public String invoke(File file, VirtualChannel virtualChannel) {
        //publish final results
        File subWorkspace = new File(file, "_Final_Detection_Results");
        try {
            if (!subWorkspace.exists()) {
                subWorkspace.mkdirs();
            }
            File reportXmlFile = new File(subWorkspace, "final_detection_result_build_" + buildNumber + ".json");
            results.writeToFile(reportXmlFile);
        } catch (IOException e) {
            logger.error("Failed to write final_detection_result file :" + e.getMessage());
        }

        return null;
    }

    @Override
    public void checkRoles(RoleChecker roleChecker) throws SecurityException {

    }
}
