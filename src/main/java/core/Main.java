package core;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        GrfcCallsignParser grfcCallsignParser = new GrfcCallsignParser("J:\\Позывные КЗС");
        List<CallsignCertificate> callsignCertificateList = grfcCallsignParser.parse();
        System.out.println(callsignCertificateList.stream().map(CallsignCertificate::toString).collect(Collectors.joining("")));
    }
}
