package lab.aikibo.epbb.controller;

import lab.aikibo.epbb.entity.JsonObject;
import lab.aikibo.epbb.service.DatObjekPajakService;
import lab.aikibo.epbb.service.DatSubjekPajakService;
import lab.aikibo.epbb.service.SpptService;
import lab.aikibo.epbb.util.DatObjekPajakModif;
import lab.aikibo.epbb.util.ReturnData;
import lab.aikibo.epbb.util.SpptModif;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;


@RestController
public class ApiController {

    private static Logger logger = Logger.getLogger(ApiController.class);

    @Autowired
    private DatObjekPajakService datOpService;

    @Autowired
    private DatSubjekPajakService datSpService;

    @Autowired
    private SpptService spptService;

    //@RequestMapping(value = "/android_service", method = RequestMethod.POST)
    public ReturnData getDataOp(@RequestBody JsonObject data) {
        String keyword = data.getKeyword();

        System.out.println(data.getSubjekPajakId());
        if(keyword.equals("op")) {
            return datOpService.getOp(data.getNop());
        } else if(keyword.equals("wp")) {
            return datSpService.getSubjekPajak(data.getSubjekPajakId());
        } else if(keyword.equals("bayar")) {
            System.out.println("nop : " + data.getNop());
            System.out.println("tahun : " + data.getTahun());
            return spptService.getPiutang(data.getNop(), data.getTahun());
        }
        return null;
    }

    //@RequestMapping(value = "/android_service/sppt", method = RequestMethod.POST)
    public List<SpptModif> getListSppt(@RequestBody JsonObject data) {
        String keyword = data.getKeyword();

        if(keyword.equals("sppt")) {
            return spptService.getSppt(data.getNop());
        }
        return null;
    }

    @RequestMapping(value = "/android_service/sppt", method = RequestMethod.POST)
    public List<SpptModif> getListSppt2(@RequestBody String data, HttpServletResponse response) {
        String keyword = "", nop = "", subjekPajakId="", tahun="";
        String [] pairs = data.split("\\&");
        HashMap<String, String> param = new HashMap<>();

        for(int i=0; i<pairs.length; i++) {
            param.put(pairs[i].split("=")[0], pairs[i].split("=")[1]);
        }

        keyword = param.get("keyword"); param.remove("keyword");
        nop = param.get("nop"); param.remove("nop");

        if(keyword.equals("sppt")) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            return spptService.getSppt(nop);
        }
        return null;
    }

    @RequestMapping(value = "/android_service", method = RequestMethod.POST)
    public ReturnData getDataOp2(@RequestBody String data, HttpServletResponse response) {
        String keyword = "", nop = "", subjekPajakId="", tahun="";
        String [] pairs = data.split("\\&");
        HashMap<String, String> param = new HashMap<>();

        for(int i=0; i<pairs.length; i++) {
            param.put(pairs[i].split("=")[0], pairs[i].split("=")[1]);
        }

        keyword = param.get("keyword"); param.remove("keyword");
        nop = param.get("nop"); param.remove("nop");
        subjekPajakId = param.get("subjek_pajak_id"); param.remove("subjek_pajak_id");
        tahun = param.get("tahun"); param.remove("tahun");

        response.setHeader("Access-Control-Allow-Origin", "*");
        if(keyword.equals("op")) {
            logger.trace("Request OP untuk NOP : " + nop);
            return datOpService.getOp(nop);
        } else if(keyword.equals("wp")) {
            logger.trace("Request SP untuk ID : " + subjekPajakId);
            return datSpService.getSubjekPajak(subjekPajakId);
        } else if(keyword.equals("bayar")) {
            logger.trace("Data Piutang untuk NOP : " + nop + " / " + tahun);
            return spptService.getPiutang(nop, tahun);
        }

        return null;
    }

}
