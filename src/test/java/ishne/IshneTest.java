package ishne;

import io.cjay.common.ecg.ishne.*;
import io.cjay.common.ecg.ishne.enumeration.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IshneTest {
    public static void main(String[] args) throws IOException {
        IshneHeader header = new IshneHeader();
        header.setFileVersion((short) 1);
        header.setFileCreateMills(System.currentTimeMillis());
        Subject subject = new Subject();
        subject.setId("Subject001");
        subject.setFirstName("San");
        subject.setLastName("Zhang");
        subject.setBirth(1300000000000L);
        subject.setSex(Sex.FEMALE);
        subject.setRace(Race.ORIENTAL);
        header.setSubject(subject);
        header.setTimeZone(null);
        header.setUnfixedLengthBlockData("Unfixed Length Block");
        header.setProprietary("Proprietary");
        header.setCopyright("Copyright");
        header.setPacemaker(Pacemaker.NONE);
        header.setRecorderType("Recorder Type");
        header.setReserved("Reserved");
        header.setEcgStartMillis(1600000000000L);
        Lead lead = new Lead(LeadType.UNKNOWN, LeadQuality.GOOD, (short) 1000);
        List<Lead> leads = new ArrayList<Lead>(1);
        leads.add(lead);
        header.setLeads(leads);
        header.setSampleRate((short) 128);
        // * 长度：采样率 * 秒数，比如 128 * 60
        int seconds = 10;
        double increment = 360.0 / header.getSampleRate();
        List<Short> ecgData = new ArrayList<>(header.getSampleRate() * seconds);
        for (int i = 0; i < seconds; ++i) {
            for (int j = 0; j < header.getSampleRate(); ++j) {
                ecgData.add((short) (header.getLeads().get(0).getAmplitude() * Math.sin(Math.toRadians(j * increment))));
            }
        }
        header.setSizeOfEcgData(ecgData.size());
        Ishne ishne = new Ishne();
        ishne.setHeader(header);
        IshneBody body = new IshneBody();
        body.setEcgData(ecgData);
        ishne.setBody(body);
        ishne.save("C:\\Users\\cjay\\Desktop\\sample.ecg");
        System.out.println("Done!!!");
    }
}