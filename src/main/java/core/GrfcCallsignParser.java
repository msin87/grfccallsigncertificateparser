package core;

import com.rtfparserkit.converter.text.StringTextConverter;
import com.rtfparserkit.parser.RtfStreamSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrfcCallsignParser {
    private String callsignFilenameHeader = "ДМТЛС";
    private static final String RTF_EXTENSION = ".rtf";
    private String pathToCallSigns;
    private File dir;
    private File[] files;
    private Pattern certificateSerialNumberPattern = Pattern.compile("(?<=№ )(СПС.*)", Pattern.MULTILINE);
    private Pattern issueDatePattern = Pattern.compile("(?<=Дата выдачи:\\W)(\\d{1,2}\\.\\d{1,2}\\.\\d{4})");
    private Pattern endDatePattern = Pattern.compile("(?<=Действительно до:\\W)(\\d{1,2}\\.\\d{1,2}\\.\\d{4})");
    private Pattern callSignPattern = Pattern.compile("(?<=\\(сигнал опознавания\\) )(\\S*)");
    private Pattern radioModelPattern = Pattern.compile("(?<=подвижной службы, )(.*)(?=,)");
    private Pattern radioSerialNumberPattern = Pattern.compile("(?<=, № )(.*)(?!\\(служба радиосвязи)");

    public GrfcCallsignParser(String pathToCallSigns) {
        this.pathToCallSigns = pathToCallSigns;
        dir = new File(pathToCallSigns);
        files = dir.listFiles((dir1, name) ->
                name.toLowerCase().endsWith(RTF_EXTENSION) && name.startsWith(callsignFilenameHeader));
    }
    public List<CallsignCertificate> parse(){
        if (files == null || files.length == 0)
            return Collections.emptyList();
        List<CallsignCertificate> callsignCertificateList = new ArrayList<>();
        for (File file : files) {
            try (InputStream inputStream = new FileInputStream(file)) {
                StringTextConverter converter = new StringTextConverter();
                converter.convert(new RtfStreamSource(inputStream));
                String extractedText = converter.getText();
                CallsignCertificate callsignCertificate = new CallsignCertificate();
                Matcher certificateSerialNumberMatcher = certificateSerialNumberPattern.matcher(extractedText);
                Matcher issueDateMatcher = issueDatePattern.matcher(extractedText);
                Matcher endDateMatcher = endDatePattern.matcher(extractedText);
                Matcher callSignMatcher = callSignPattern.matcher(extractedText);
                Matcher radioModelMatcher = radioModelPattern.matcher(extractedText);
                Matcher radioSerialNumberMatcher = radioSerialNumberPattern.matcher(extractedText);
                if (!(certificateSerialNumberMatcher.find() && issueDateMatcher.find() && endDateMatcher.find()
                        && callSignMatcher.find() && radioModelMatcher.find() && radioSerialNumberMatcher.find()))
                    continue;
                callsignCertificate.setCertificateSerialNumber(certificateSerialNumberMatcher.group().trim());
                callsignCertificate.setIssueDate(issueDateMatcher.group().trim());
                callsignCertificate.setEndDate(endDateMatcher.group().trim());
                callsignCertificate.setCallSign(callSignMatcher.group().trim());
                callsignCertificate.setRadioModel(radioModelMatcher.group().trim());
                callsignCertificate.setRadioSerialNumber(radioSerialNumberMatcher.group().trim());
                callsignCertificateList.add(callsignCertificate);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return callsignCertificateList;
    }
}
