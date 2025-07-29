/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos;

import com.gsma.sgp.messages.pkix1implicit88.SubjectKeyIdentifier;
import com.gsma.sgp.messages.rspdefinitions.EUICCInfo2;
import com.gsma.sgp.messages.rspdefinitions.VersionType;

import java.util.ArrayList;
import java.util.List;

public class EuiccInfo {
    private static final String TAG = EuiccInfo.class.getName();

    private String eid;
    private final String profileVersion;
    private final String svn;
    private final String euiccFirmwareVer;
    private final String globalplatformVersion;
    private final String sasAcreditationNumber;
    private final List<String> pkiIds;

    public EuiccInfo(EUICCInfo2 euiccInfo2) {
        this(null, euiccInfo2);
    }

    public EuiccInfo(String eid, EUICCInfo2 euiccInfo2) {
        this.eid = eid;
        this.profileVersion = versionTypeToString(euiccInfo2.getBaseProfilePackageVersion());
        this.svn = versionTypeToString(euiccInfo2.getLowestSvn());
        this.euiccFirmwareVer = versionTypeToString(euiccInfo2.getEuiccFirmwareVersion());
        this.globalplatformVersion = versionTypeToString(euiccInfo2.getGlobalplatformVersion());

        this.sasAcreditationNumber = euiccInfo2.getSasAcreditationNumber().toString();

        this.pkiIds = euiccPkiIdList(euiccInfo2.getEuiccCiPKIdListForSigning());
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getEid() {
        return eid;
    }

    public String getProfileVersion() {
        return profileVersion;
    }

    public String getSvn() {
        return svn;
    }

    public String getEuiccFirmwareVer() {
        return euiccFirmwareVer;
    }


    public String getGlobalplatformVersion() {
        return globalplatformVersion;
    }

    public String getSasAcreditationNumber() {
        return sasAcreditationNumber;
    }

    public List<String> getPkiIds() {
        return pkiIds;
    }

    public String getPkiIdsAsString(){
        StringBuilder sb = new StringBuilder();

        if(pkiIds.isEmpty()) {
            return "";
        }

        for(String pkiId : pkiIds) {
            sb.append(pkiId);
            sb.append("\n");
        }

        return sb.subSequence(0, sb.length() - 1).toString();
    }

    private static String versionTypeToString(VersionType versionType) {
        if(versionType != null) {
            return versionType.toString();
        }

        return "N/A";
    }

    private static List<String> euiccPkiIdList(EUICCInfo2.EuiccCiPKIdListForSigning pkiIdListIn) {
        List<String> pkiIdList = new ArrayList<>();

        for(SubjectKeyIdentifier ski : pkiIdListIn.getSubjectKeyIdentifier()) {
            pkiIdList.add(ski.toString());
        }

        return pkiIdList;
    }


}
