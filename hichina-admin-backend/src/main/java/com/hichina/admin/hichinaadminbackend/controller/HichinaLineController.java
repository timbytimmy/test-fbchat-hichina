package com.hichina.admin.hichinaadminbackend.controller;

import com.github.pagehelper.PageHelper;
import com.hichina.admin.hichinaadminbackend.mapper.HichinaLineMapper;
import com.hichina.admin.hichinaadminbackend.model.DTO.HichinaResponse;
import com.hichina.admin.hichinaadminbackend.model.DTO.LineBatchDeleteRequest;
import com.hichina.admin.hichinaadminbackend.model.DTO.LineRequest;
import com.hichina.admin.hichinaadminbackend.model.DTO.PaginationWrapper;
import com.hichina.admin.hichinaadminbackend.model.HichinaLine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/line")
public class HichinaLineController {

    @Autowired
    private HichinaLineMapper hichinaLineMapper;

    @Autowired
    private Environment env;

    @PutMapping("/{publicId}")
    public HichinaResponse updateLine(@PathVariable("publicId") String publicId , @RequestBody LineRequest request){
        HichinaResponse ret = new HichinaResponse();
        List<HichinaLine> lines = hichinaLineMapper.findLineByPublicId(publicId);

        if(lines.isEmpty()){
            throw new RuntimeException("No line found by publicId: "+ publicId);
        }

        HichinaLine toBeUpdated = lines.get(0);

        toBeUpdated.setName(request.getName());
        toBeUpdated.setDescription(request.getDescription());
        toBeUpdated.setBasePrice(request.getBasePrice());
        toBeUpdated.setRangeInDays(request.getRangeInDays());
        toBeUpdated.setIconPath(request.getIconPath());

        hichinaLineMapper.update(toBeUpdated);

        ret.setMessage("成功Update publicId为"+publicId+"的记录");
        ret.setData(publicId);
        ret.setOk(true);

        return ret;
    }

    @DeleteMapping("/batch")
    public HichinaResponse deleteLines(@RequestBody LineBatchDeleteRequest req){

        hichinaLineMapper.batchDelete(req.getToDelete());

        HichinaResponse ret = new HichinaResponse();
        ret.setOk(true);
        ret.setData(req.getToDelete());
        ret.setMessage("成功批量删除");

        return ret;
    }

    @PostMapping
    public HichinaResponse createLine(@RequestBody LineRequest params){
        HichinaResponse ret = new HichinaResponse();

        HichinaLine line = new HichinaLine();
        line.setBasePrice(params.getBasePrice());
        line.setCreatedDate(new Date());
        line.setName(params.getName());
        line.setDescription(params.getDescription());
        line.setIconPath(params.getIconPath());
        line.setRangeInDays(params.getRangeInDays());
        line.setPublicId(java.util.UUID.randomUUID().toString());
        hichinaLineMapper.insert(line);

        ret.setOk(true);
        ret.setData(line.getId());
        ret.setMessage("成功新建线路");

        return ret;
    }

    @GetMapping("/{publicId}")
    public HichinaResponse getSingleLineById(@PathVariable("publicId") String publicId){
        HichinaResponse ret = new HichinaResponse();
        List<HichinaLine> lines = hichinaLineMapper.findLineByPublicId(publicId);

        ret.setOk(true);
        ret.setData(lines.isEmpty()?null:lines.get(0));
        ret.setMessage("获取publicId为"+publicId+"的线路");

        return ret;
    }

    @GetMapping
    public HichinaResponse getListOfLinesPagedWithSearchKeyword(@RequestParam(value = "page", required = true) Integer page,
                                                                @RequestParam(value = "pageSize", required = true) Integer size,
                                                                @RequestParam(value = "query") String query){
        HichinaResponse ret = new HichinaResponse();
        PageHelper.startPage(page,size);
        //int defaultPageSize = env.getProperty("default.page.size", Integer.class)PageHelper.startPage(page,size);

        List<HichinaLine> rawData;
        int cnt = 0;
        if(!StringUtils.isEmpty(query)){
            rawData = hichinaLineMapper.findLinesByQuery(query);
            cnt = hichinaLineMapper.countByQuery(query);
        }else{
            rawData = hichinaLineMapper.findAllLines();
            cnt = hichinaLineMapper.count();
        }
        ret.setMessage("Successfully got all lines");
        PaginationWrapper paginationWrapper = new PaginationWrapper();
        paginationWrapper.setData(rawData);
        paginationWrapper.setPageSize(size);
        paginationWrapper.setCurrentPage(page);
        paginationWrapper.setTotal(cnt);
        ret.setData(paginationWrapper);
        ret.setOk(true);

        return ret;
    }
}
