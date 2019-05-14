package cn.com.meguru;

import cn.com.meguru.dao.mapper.SourceOfStudentMapper;
import cn.com.meguru.dao.mapper.StatusOfStudentMapper;
import cn.com.meguru.dao.mapper.StatusOfStudentMapperEx;
import cn.com.meguru.dao.mapper.StudentMapper;
import cn.com.meguru.entity.SourceOfStudent;
import cn.com.meguru.entity.StatusOfStudent;
import cn.com.meguru.entity.Student;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class GetService {

    @Autowired
    StatusOfStudentMapper statusOfStudentMapper;
    @Autowired
    SourceOfStudentMapper sourceOfStudentMapper;
    @Autowired
    StatusOfStudentMapperEx statusOfStudentMapperEx;
    @Autowired
    StudentMapper studentMapper;

    public void get(String account) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequestPost = HttpRequest.newBuilder(URI.create("http://www.ihubu.cn/"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwUKLTYzNzE1NjIwOWRku1ea9OJ5EjzaI3%2BV9IHN%2F2tvJdyY1Tt8L%2BQhgS7lYAY%3D&__VIEWSTATEGENERATOR=C2EE9ABB&__EVENTVALIDATION=%2FwEdAATFWYcz9xcOBQDW1Klip7nw8x5TPe4Fb2SCxWQFXQqD6Fz4Ff%2FmRdr9eJovHJ26GXDQt9u8Yj9aTUScKk9HMLRqDNJGFjlWeNherQcAcz9OZsluvJbYG4%2FkQ0%2BqG0g9ZEQ%3D&Login1%24UserName="
                                + account
                                + "&Login1%24Password=123456&Login1%24LoginButton=%E7%99%BB%E5%BD%95"
                ))
                .build();
        HttpResponse<String> www_ihubu_cn = null;
            www_ihubu_cn = httpClient.send(httpRequestPost, HttpResponse.BodyHandlers.ofString());
        List<String> coolies = www_ihubu_cn.headers().map().get("set-cookie");
        if (coolies.size() == 1) return;

        HttpRequest httpRequestGet = HttpRequest.newBuilder(URI.create("http://www.ihubu.cn/GraduateUser/SourceInfoFill.aspx"))
                .header("Cookie", coolies.get(0) + "; " + coolies.get(1))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();
        HttpResponse<String> www_ihubu_cn_get = null;
            www_ihubu_cn_get = httpClient.send(httpRequestGet, HttpResponse.BodyHandlers.ofString());
        String body = www_ihubu_cn_get.body();
        parser(body);
    }

    public List<String> getFacultyProfessionCode() {
        List<String> facultyProfessionCode = statusOfStudentMapperEx.getFacultyProfessionCode();
        return facultyProfessionCode;
    }

    private void parser(String body) throws IOException {
        Document document = Jsoup.parse(body);
        Elements elements = document.getElementsByClass("dxeEditArea_Aqua dxeEditAreaSys");

//        StatusOfStudent statusOfStudent = toStatusOfStudent(elements);
//        SourceOfStudent sourceOfStudent = toSourceOfStudent(elements);
//        statusOfStudentMapper.insertSelective(statusOfStudent);
//        sourceOfStudentMapper.insertSelective(sourceOfStudent);
        Student student = toStudent(elements);
        studentMapper.insertSelective(student);

//        BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get("J:\\workspace\\get-hubu-student-infomation\\src\\main\\resources\\help.txt"));
//        for (int i = 0; i < elements.size(); i++) {
//            bufferedWriter.write(i + "\n" + elements.get(i) + "\n\n");
//            bufferedWriter.flush();
//        }
//        bufferedWriter.close();
    }

    private Student toStudent(Elements elements) {
        Student student = new Student();
        student.setCollege(elements.get(0).val());
        student.setFaculty(elements.get(1).val());
        student.setStudentId(elements.get(2).val());
        student.setName(elements.get(3).val());
        student.setIdCard(elements.get(4).val());
        student.setGender(elements.get(5).val().equals("男") ? "0" : "1");
        student.setEducation(elements.get(6).val());
        student.setProfession(elements.get(7).val());
        student.setEducationalSystem(Byte.valueOf(elements.get(8).val()));
        student.setDegree(elements.get(9).val());
        student.setTrainingMethod(elements.get(10).val());
        student.setGraduationYear(elements.get(11).val());
        student.setProfessionalDirection(elements.get(12).val());
        student.setMaritalStatus(elements.get(13).val());
        student.setPoliticalStatus(elements.get(14).val());
        student.setNationality(elements.get(15).val());
        student.setDateOfBirth(parseDate(elements.get(16).val()));
        student.setDateOfStartSchool(parseDate(elements.get(17).val()));
        student.setMajoringInForeignLanguage(elements.get(18).val());
        student.setCityOrVillage(elements.get(19).val().equals("城镇") ? "0" : "1");
        student.setAccountToSchool(elements.get(20).val().equals("否") ? "0" : "1");
        student.setArchivesToSchool(elements.get(21).val().equals("否") ? "0" : "1");
        student.setMajoringInForeignLanguageLevel(elements.get(22).val());
        student.setHomeAddress(elements.get(23).val());
        student.setStudentArea(elements.get(24).val());
        student.setOther1(elements.get(25).val());
        student.setOther2(elements.get(26).val());
        student.setHomeZipCode(elements.get(27).val());
        student.setIsTeacherTraining(elements.get(28).equals("非师范") ? "0" : "1");
        student.setQq(elements.get(29).val());
        student.setFamilyContact(elements.get(30).val());
        student.setFamilyPhone(elements.get(31).val());
        student.setIntentionArea(elements.get(32).val());
        student.setOther3(elements.get(33).val());
        student.setOther4(elements.get(34).val());
        student.setIntentionalUnitNature(elements.get(35).val());
        student.setIntentionalUnitIndustry(elements.get(36).val());
        student.setIntentionalJobCategory(elements.get(37).val());
        student.setIntentionalMonthlySalary(elements.get(38).val());
        student.setEmail(elements.get(39).val());
        student.setPhone(elements.get(40).val());
        return student;
    }

//    private StatusOfStudent toStatusOfStudent(Elements elements) {
//        StatusOfStudent statusOfStudent = new StatusOfStudent();
//        statusOfStudent.setCollege(elements.get(0).val());
//        statusOfStudent.setFaculty(elements.get(1).val());
//        statusOfStudent.setStudentId(elements.get(2).val());
//        statusOfStudent.setName(elements.get(3).val());
//        statusOfStudent.setIdCard(elements.get(4).val());
//        statusOfStudent.setGender(elements.get(5).val().equals("男") ? "0" : "1");
//        statusOfStudent.setEducation(elements.get(6).val());
//        statusOfStudent.setProfession(elements.get(7).val());
//        return statusOfStudent;
//    }

//    private SourceOfStudent toSourceOfStudent(Elements elements) {
//        SourceOfStudent sourceOfStudent = new SourceOfStudent();
//        sourceOfStudent.setEducationalSystem(Byte.valueOf(elements.get(8).val()));
//        sourceOfStudent.setDegree(elements.get(9).val());
//        sourceOfStudent.setTrainingMethod(elements.get(10).val());
//        sourceOfStudent.setGraduationYear(elements.get(11).val());
//        sourceOfStudent.setProfessionalDirection(elements.get(12).val());
//        sourceOfStudent.setMaritalStatus(elements.get(13).val());
//        sourceOfStudent.setPoliticalStatus(elements.get(14).val());
//        sourceOfStudent.setNationality(elements.get(15).val());
//        sourceOfStudent.setDateOfBirth(parseDate(elements.get(16).val()));
//        sourceOfStudent.setDateOfStartSchool(parseDate(elements.get(17).val()));
//        sourceOfStudent.setMajoringInForeignLanguage(elements.get(18).val());
//        sourceOfStudent.setCityOrVillage(elements.get(19).val().equals("城镇") ? "0" : "1");
//        sourceOfStudent.setAccountToSchool(elements.get(20).val().equals("否") ? "0" : "1");
//        sourceOfStudent.setArchivesToSchool(elements.get(21).val().equals("否") ? "0" : "1");
//        sourceOfStudent.setMajoringInForeignLanguageLevel(elements.get(22).val());
//        sourceOfStudent.setHomeAddress(elements.get(23).val());
//        sourceOfStudent.setStudentArea(elements.get(24).val());
//        sourceOfStudent.setOther1(elements.get(25).val());
//        sourceOfStudent.setOther2(elements.get(26).val());
//        sourceOfStudent.setHomeZipCode(elements.get(27).val());
//        sourceOfStudent.setIsTeacherTraining(elements.get(28).equals("非师范") ? "0" : "1");
//        sourceOfStudent.setQq(elements.get(29).val());
//        sourceOfStudent.setFamilyContact(elements.get(30).val());
//        sourceOfStudent.setFamilyPhone(elements.get(31).val());
//        sourceOfStudent.setIntentionArea(elements.get(32).val());
//        sourceOfStudent.setOther3(elements.get(33).val());
//        sourceOfStudent.setOther4(elements.get(34).val());
//        sourceOfStudent.setIntentionalUnitNature(elements.get(35).val());
//        sourceOfStudent.setIntentionalUnitIndustry(elements.get(36).val());
//        sourceOfStudent.setIntentionalJobCategory(elements.get(37).val());
//        sourceOfStudent.setIntentionalMonthlySalary(elements.get(38).val());
//        sourceOfStudent.setEmail(elements.get(39).val());
//        sourceOfStudent.setPhone(elements.get(40).val());
//        return sourceOfStudent;
//    }

    private Date parseDate(String s) {
        LocalDate localDate = LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyyMMdd"));
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }
}
