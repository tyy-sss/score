package com.example.go.controller;

import com.example.go.common.MessageNews;
import com.example.go.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping
public class UploadController {

    @Value("${file.uploadFolder}")
    private String baseLocation;

    /**
     * 文件上传
     * @param req
     * @return
     */
    @PostMapping("/upload")
    public R upload(MultipartHttpServletRequest multipartHttpServletRequest, HttpServletRequest req){
        //得到传过来的文件
        Map<String,MultipartFile> files = multipartHttpServletRequest.getFileMap();
        //得到头像文件
        MultipartFile file = files.get("avatar");
        if(!file.isEmpty()){
            System.out.println("文件不为空");
            //原始文件名
            String originFileName = file.getOriginalFilename();
            //截取原始文件名的后缀
            String suffix = originFileName.substring(originFileName.lastIndexOf('.'));
            //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
            String fileName = UUID.randomUUID().toString().replace("-","") +suffix;
            //创建一个目录对象
            File dir = new File(baseLocation);
            //判断basePath是否存在
            if(!dir.exists()){
                //如果不存在，就创建
                dir.mkdirs();
            }
            try {
                //转存文件到达指定位置
                file.transferTo(new File(dir,fileName));
            } catch (IOException e) {
               e.printStackTrace();
                return R.error(MessageNews.MESSAGE_FAIL_UPLOAD_FAIL);
            }
            String address = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/api/file/" + fileName ;
            //上传地址在浏览器中打开的路径
            System.out.println(address);
            return R.success(fileName);
        }
        return R.error(MessageNews.MESSAGE_FAIL_UPLOAD_FAIL);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void downLoad(String name, HttpServletResponse response){
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(baseLocation + name));
            //输出流，通过输出流把文件写会浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while( (len = fileInputStream.read(bytes)) !=-1){
                outputStream.write(bytes,0 ,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
