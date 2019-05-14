package cn.com.meguru;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@MapperScan("cn.com.meguru.dao.mapper")
public class GetHubuStudentInfomationServerApplication {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(GetHubuStudentInfomationServerApplication.class, args);
        GetService getService = context.getBean(GetService.class);
//        2015//入学年份 22 1119//学院 20//专业 0128//人头号
//        getService.get("2015221119200125");

// ------------------------------------------------生成学号抓取数据------------------------------------------------------
//        Path countPath1 = Paths.get("J:\\workspace\\get-hubu-student-infomation\\src\\main\\resources\\count1.txt");
//        Path countPath2 = Paths.get("J:\\workspace\\get-hubu-student-infomation\\src\\main\\resources\\count2.txt");
//        Path countPath3 = Paths.get("J:\\workspace\\get-hubu-student-infomation\\src\\main\\resources\\count3.txt");
//        Path countPath4 = Paths.get("J:\\workspace\\get-hubu-student-infomation\\src\\main\\resources\\count4.txt");
//        Path errPath1 = Paths.get("J:\\workspace\\get-hubu-student-infomation\\src\\main\\resources\\err1.txt");
//        Path errPath2 = Paths.get("J:\\workspace\\get-hubu-student-infomation\\src\\main\\resources\\err2.txt");
//        Path errPath3 = Paths.get("J:\\workspace\\get-hubu-student-infomation\\src\\main\\resources\\err3.txt");
//        Path errPath4 = Paths.get("J:\\workspace\\get-hubu-student-infomation\\src\\main\\resources\\err4.txt");

//        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
        //20152211 05 99 0500
//        fixedThreadPool.execute(new Get(4, 98, 20, 5, 99, 500, countPath1, errPath1, getService));
        //20152211 10 99 0500
//        fixedThreadPool.execute(new Get(9, 92, 52, 10, 99, 500, countPath2, errPath2, getService));
        //20152211 15 32 0094
//        fixedThreadPool.execute(new Get(14, 65, 0, 15, 99, 500, countPath3, errPath3, getService));

//        fixedThreadPool.execute(new Get(14, 31, 93, 15, 65, 500, countPath2, errPath2, getService));
//        fixedThreadPool.execute(new Get(14, 65, 0, 15, 99, 500, countPath3, errPath3, getService));

        //20152211 20 13 0489
//        fixedThreadPool.execute(new Get(17, 37, 179, 20, 99, 500, countPath4, errPath4, getService));

//        fixedThreadPool.execute(new Get(19, 12, 488, 20, 35, 500, countPath1, errPath1, getService));
//        fixedThreadPool.execute(new Get(19, 35, 0, 20, 55, 500, countPath2, errPath2, getService));
//        fixedThreadPool.execute(new Get(19, 55, 0, 20, 75, 500, countPath3, errPath3, getService));
//        fixedThreadPool.execute(new Get(19, 75, 0, 20, 99, 500, countPath4, errPath4, getService));
// ------------------------------------------------生成学号抓取数据------------------------------------------------------

// ------------------------------------------------重新查询出错学号------------------------------------------------------
        new Thread(new GetErr(getService)).run();
// ------------------------------------------------重新查询出错学号------------------------------------------------------

// ------------------------------------------------根据学院/专业抓取数据-------------------------------------------------
        //11xx xx
//        List<String> facultyProfessionCode = getService.getFacultyProfessionCode();
//
//        BufferedWriter err = null;
//        BufferedWriter count = null;
//
//        err = Files.newBufferedWriter(Paths.get("J:\\workspace\\get-hubu-student-infomation\\src\\main\\resources\\err_student_id.txt"));
//        count = Files.newBufferedWriter(Paths.get("J:\\workspace\\get-hubu-student-infomation\\src\\main\\resources\\count5.txt"));
//
//        for (int i = 0; i < facultyProfessionCode.size(); i++) {
//            for (int j = 1; j < 500; j++) {
//                StringBuilder stringBuilder = new StringBuilder("201522").append(facultyProfessionCode.get(i));
//                if (j < 10) stringBuilder.append("000").append(j);
//                else if (j < 100) stringBuilder.append("00").append(j);
//                else stringBuilder.append("0").append(j);
//                String studentId = stringBuilder.toString();
//                try {
//                    getService.get(studentId);
//                    count.write(studentId + "\n");
//                    count.flush();
//                } catch (Exception e) {
//                    err.write(studentId + "\n");
//                    err.flush();
//                    e.printStackTrace();
//                }
//            }
//        }
// ------------------------------------------------根据学院/专业抓取数据-------------------------------------------------
    }

    public static class GetErr implements Runnable {
        GetService getService;

        @Override
        public void run() {
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;
            BufferedWriter count5 = null;
            String studentId = null;

            try {
                bufferedReader = Files.newBufferedReader(Paths.get("J:\\workspace\\get-hubu-student-infomation\\src\\main\\resources\\err.txt"));
                bufferedWriter = Files.newBufferedWriter(Paths.get("J:\\workspace\\get-hubu-student-infomation\\src\\main\\resources\\err_student_id.txt"));
                count5 = Files.newBufferedWriter(Paths.get("J:\\workspace\\get-hubu-student-infomation\\src\\main\\resources\\count5.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                while (Objects.nonNull(studentId = bufferedReader.readLine())) {
                    try {
                        studentId = "201522" + studentId;
                        count5.write(studentId + "\n");
                        count5.flush();
                        getService.get(studentId);
                    } catch (IOException e) {
                        bufferedWriter.write(studentId + "\n");
                        bufferedWriter.flush();
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public GetErr(GetService getService) {
            this.getService = getService;
        }
    }

    public static class Get implements Runnable {
        //        int facultyCodeStart = 0, professionCodeStart = 0, studentCodeStart = 0;
//        int facultyCodeEnd = 20, professionCodeEnd = 99, studentCodeEnd = 499;
        int facultyCodeStart, professionCodeStart, studentCodeStart;
        int facultyCodeEnd, professionCodeEnd, studentCodeEnd;
        Path countPath, errPath;
        GetService getService;

        @Override
        public void run() {
            BufferedWriter countWriter = null;
            BufferedWriter errWriter = null;
            try {
                countWriter = Files.newBufferedWriter(countPath);
                errWriter = Files.newBufferedWriter(errPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (facultyCodeStart++ < facultyCodeEnd) {
                while (professionCodeStart++ < professionCodeEnd) {
                    while (studentCodeStart++ < studentCodeEnd) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(20152211);
                        if (facultyCodeStart < 10) stringBuilder.append(0).append(facultyCodeStart);
                        else stringBuilder.append(facultyCodeStart);
                        if (professionCodeStart < 10) stringBuilder.append(0).append(professionCodeStart);
                        else stringBuilder.append(professionCodeStart);
                        stringBuilder.append(0);
                        if (studentCodeStart < 10) stringBuilder.append("00").append(studentCodeStart);
                        else if (studentCodeStart < 100) stringBuilder.append(0).append(studentCodeStart);
                        else stringBuilder.append(studentCodeStart);
                        String studentId = stringBuilder.toString();
                        try {
                            countWriter.write(studentId + "\n");
                            countWriter.flush();
                            try {
                                getService.get(studentId);

                            } catch (IOException e) {
                                errWriter.write(studentId + "\n");
                                errWriter.flush();
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                errWriter.write(studentId + "\n");
                                errWriter.flush();
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    studentCodeStart = 0;
                }
                professionCodeStart = 0;
            }
            try {
                countWriter.close();
                errWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Get(int facultyCodeStart, int professionCodeStart, int studentCodeStart, int facultyCodeEnd, int professionCodeEnd, int studentCodeEnd, Path countPath, Path errPath, GetService getService) {
            this.facultyCodeStart = facultyCodeStart;
            this.professionCodeStart = professionCodeStart;
            this.studentCodeStart = studentCodeStart;
            this.facultyCodeEnd = facultyCodeEnd;
            this.professionCodeEnd = professionCodeEnd;
            this.studentCodeEnd = studentCodeEnd;
            this.countPath = countPath;
            this.errPath = errPath;
            this.getService = getService;
        }
    }
}

