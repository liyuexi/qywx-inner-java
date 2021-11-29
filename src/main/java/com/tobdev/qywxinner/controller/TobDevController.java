package com.tobdev.qywxinner.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tobdev.qywxinner.model.entity.QywxInnerCompany;
import com.tobdev.qywxinner.model.entity.QywxInnerUser;
import com.tobdev.qywxinner.service.QywxInnerCompanyService;
import com.tobdev.qywxinner.service.QywxInnerService;
import com.tobdev.qywxinner.utils.JWTUtils;
import com.tobdev.qywxinner.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class TobDevController {


    @Autowired
    private QywxInnerCompanyService qywxInnerCompanyService;
    @Value("${tobdev-upload.tcb-id}")
    public String tcbId;
    @Value("${tobdev-upload.tcb-key}")
    public String tcbKey;
    @Value("${tobdev-upload.tcb-env}")
    public String tcbEnv;
    @Value("${tobdev-upload.tcb-local-path}")
    public String tcbLocalPath;
    @Value("${tobdev-upload.tcb-cloud-path}")
    public String tcbCloudPath;

    @RequestMapping("/tobdev/verify")
    @ResponseBody()
    JsonData index(){
        HashMap resData = new HashMap();
        resData.put("txt","接口验证成功 tobdev.com 关注ToB Dev公众号，加李月喜微信li570467731进企微开发同行群");
        return   JsonData.buildSuccess(resData);
    }

    @PostMapping({"/tobdev/set"})
    JsonData setAgent(HttpServletRequest request , @RequestBody Map map){

        String corpId = (String) map.get("corp_id");
        String agentId =  (String)map.get("agent_id");
        String secret =  (String)map.get("agent_secret");
        QywxInnerCompany company = new QywxInnerCompany();
        company.setCorpId(corpId);
        company.setAgentId(agentId);
        company.setAgentSecret(secret);
        company.setStatus(1);
        Integer res =   qywxInnerCompanyService.saveCompany(company);
        return    JsonData.buildSuccess(res);

    }

    @PostMapping({"/tobdev/verifyFile"})
    JsonData upload(HttpServletRequest request ,@RequestParam("file") MultipartFile file){

//         System.out.print("传入的文件参数：{}"+JSON.toJSONString(file, true));
        if (Objects.isNull(file) || file.isEmpty()) {
            System.out.print("传入的文件为空");
            JsonData.buildError("文件为空，请重新上传");
        }
        String fileName= file.getOriginalFilename();
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(this.tcbLocalPath +fileName);
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(Paths.get(this.tcbCloudPath));
            }
            //文件写入指定路径
            Files.write(path, bytes);
            System.out.print("保存本地成功");
            //return "文件上传成功";
        } catch (IOException e) {
            e.printStackTrace();
            //return "后端异常...";
            return    JsonData.buildError(e.getMessage());
        }
//       return JsonData.buildSuccess();

        ProcessBuilder processBuilder = new ProcessBuilder();
        //定义命令内容        cloudbase hosting:deploy localPath cloudPath -e envId
//        ArrayList<String> command = new ArrayList<String>();
//        command.add("cloudbase");
//        command.add("hosting:deploy");
//        command.add(this.tcbLocalPath);
//        command.add(this.tcbCloudPath);
//        command.add("-e");
//        command.add(this.tcbEnv+"testxxx.png");
        String loginStr = "cloudbase login "+ " --apiKeyId "+this.tcbId+" --apiKey "+this.tcbKey;
        String deployStr = "cloudbase hosting:deploy "+ this.tcbLocalPath+fileName + " "+this.tcbCloudPath+fileName+" -e "+this.tcbEnv;
        String commandStr = loginStr+" && "+deployStr;
        System.out.print(commandStr);

        ArrayList<String> command = new ArrayList<String>();
        command.add("bash");
        command.add("-c");
        command.add(commandStr);

        processBuilder.command(command);
        //将标准输入流和错误输入流合并，通过标准输入流读取信息
        processBuilder.redirectErrorStream(true);
        try {
            //启动进程
            Process start = processBuilder.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(start.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = start.waitFor();
            System.out.println("exitCode = "+exitCode);


//            //获取输入流
//            InputStream inputStream = start.getInputStream();
//            //转成字符输入流
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf8");
//            int len = -1;
//            char[] c = new char[1024];
//            StringBuffer outputString = new StringBuffer();
//            //读取进程输入流中的内容
//            while ((len = inputStreamReader.read(c)) != -1) {
//                String s = new String(c, 0, len);
//                outputString.append(s);
//                System.out.print(s);
//            }
//            inputStream.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return    JsonData.buildError(e.getMessage());
        }
        return    JsonData.buildSuccess();
    }


}
