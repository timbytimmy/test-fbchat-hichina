package com.hichina.admin.hichinaadminbackend.controller;

import com.github.pagehelper.PageHelper;
import com.hichina.admin.hichinaadminbackend.mapper.DestinationMapper;
import com.hichina.admin.hichinaadminbackend.mapper.ProductSkuGroupDestinationMappingMapper;
import com.hichina.admin.hichinaadminbackend.model.DTO.*;
import com.hichina.admin.hichinaadminbackend.model.Destination;
import com.hichina.admin.hichinaadminbackend.model.ProductSkuGroupDestinationMapping;
import com.hichina.admin.hichinaadminbackend.service.HichinaOpenAiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/destination")
public class DestinationController {

    @Autowired
    private DestinationMapper destinationMapper;

    @Autowired
    private HichinaOpenAiService hichinaOpenAiService;

    @Autowired
    private ProductSkuGroupDestinationMappingMapper productSkuGroupDestinationMappingMapper;

    @PostMapping("/bind-skugroup-destination")
    public HichinaResponse bindSkuGroupAndDestination(@RequestBody SkuGroupDestinationBindRequest request){
        HichinaResponse ret = new HichinaResponse();

        ProductSkuGroupDestinationMapping newObj = new ProductSkuGroupDestinationMapping();
        newObj.setSkuGroupId(request.getSkuGroupId());
        newObj.setDestinationId(request.getDestinationId());
        productSkuGroupDestinationMappingMapper.insert(newObj);

        ret.setOk(true);
        ret.setMessage("Succceed binding destination and product sku group");

        return ret;
    }

    @PutMapping("/openai-gen-desc/{destinationId}")
    public HichinaResponse updateDestinationDecriptionWithOpenAI(@PathVariable("destinationId") String destinationId){
        HichinaResponse ret = new HichinaResponse();

        List<Destination> destinations = destinationMapper.findDestinationById(destinationId);
        if(destinations.isEmpty()){
            throw new RuntimeException("Nothing to update");
        }
        Destination toUpdate = destinations.get(0);
        String generatedDescription = hichinaOpenAiService.generateShortDescription(toUpdate.getDestinationName());
        toUpdate.setDescription(generatedDescription);
        destinationMapper.update(toUpdate);
        ret.setOk(true);
        ret.setData(destinationId);
        ret.setMessage("成功用openAi生成目的地描述");
        return ret;

    }

    @DeleteMapping("/unbind-skugroup-destination")
    public HichinaResponse unbindSkuGroupAndDestination(@RequestBody SkuGroupDestinationBindRequest request){
        HichinaResponse ret = new HichinaResponse();

        productSkuGroupDestinationMappingMapper.delete(request.getSkuGroupId(), request.getDestinationId());

        ret.setOk(true);
        ret.setMessage("Succceed unbinding destination and product sku group");

        return ret;
    }

    @PostMapping
    public HichinaResponse createDestination(@RequestBody DestinationCreateDTO request){
        HichinaResponse ret = new HichinaResponse();

        Destination destination = new Destination();
        destination.setDestinationId(java.util.UUID.randomUUID().toString());
        destination.setDestinationName(request.getName());
        destination.setDescription(request.getDescription());
        destination.setLevel(request.getLevel());
        destination.setParentId(request.getParentId());
        destination.setDestinationProfileImage(request.getDestinationProfileImage());
        destination.setCreatedDate(new Date());

        destinationMapper.insert(destination);

        ret.setData(destination);
        ret.setOk(true);
        ret.setMessage("成功新建目的地");

        return ret;
    }

    @DeleteMapping("/batch")
    @Transactional
    public HichinaResponse deleteDestinations(@RequestBody DestinationBatchDeleteRequest req){

        // delete base product record
        destinationMapper.batchDelete(req.getToDelete());

        HichinaResponse ret = new HichinaResponse();
        ret.setOk(true);
        ret.setData(req.getToDelete());
        ret.setMessage("成功批量删除目的地");

        return ret;
    }

    @PutMapping("/{destinationId}")
    @Transactional
    public HichinaResponse updateDestination(@PathVariable("destinationId") String destinationId ,@RequestBody DestinationUpdateDTO request){
        HichinaResponse ret = new HichinaResponse();

        List<Destination> destinations = destinationMapper.findDestinationById(destinationId);
        if(destinations.isEmpty()){
            throw new RuntimeException("Nothing to update");
        }
        Destination toUpdate = destinations.get(0);

        toUpdate.setLevel(request.getLevel());
        toUpdate.setDestinationName(request.getName());
        toUpdate.setDescription(request.getDescription());
        toUpdate.setDestinationProfileImage(request.getDestinationProfileImage());
        toUpdate.setParentId(request.getParentId());

        // update basic info
        destinationMapper.update(toUpdate);

        ret.setOk(true);
        ret.setData(destinationId);
        ret.setMessage("成功更新destination:"+destinationId+"的目的地");

        return ret;
    }

    @GetMapping("/{destinationId}")
    public Destination getByDestinationId(@PathVariable("destinationId") String destinationId){
        List<Destination> destinations = destinationMapper.findDestinationById(destinationId);

        if(destinations.isEmpty()){
            throw new RuntimeException("destination not found");
        }
        return destinations.get(0);
    }

    @GetMapping("/sku-group-binded/{skuGroupId}")
    public HichinaResponse getBindedDestinationsBySkuGroupId(@PathVariable("skuGroupId") String skuGroupId){
        HichinaResponse ret = new HichinaResponse();
        List<DestinationShortDTO> destinationShortDTOS = productSkuGroupDestinationMappingMapper.getShortDestinationsBySkuGroupId(skuGroupId);

        ret.setMessage("Succeed get binded destinations");
        ret.setOk(true);
        ret.setData(destinationShortDTOS);
        return ret;
    }

    @GetMapping
    public HichinaResponse getDestinations(@RequestParam(value = "page", required = true) Integer page,
                                                           @RequestParam(value = "pageSize", required = true) Integer size,
                                                           @RequestParam(value = "query") String query,
                                           @RequestParam(value = "level") Integer level){
        HichinaResponse ret = new HichinaResponse();
        if(page>0){
            PageHelper.startPage(page,size);
        }

        List<Destination> rawData;
        int cnt = 0;
        if(!StringUtils.isEmpty(query)){
            if(level!=null && level!=-1){
                rawData = destinationMapper.findDestinationsByQueryAndLevel(query, level);
                cnt = destinationMapper.countByQueryAndLevel(query, level);
            }else{
                rawData = destinationMapper.findDestinationsByQuery(query);
                cnt = destinationMapper.countByQuery(query);
            }
        }else{
            if(level!=null && level!=-1){
                rawData = destinationMapper.findDestinationsByLevel(level);
                cnt = destinationMapper.countByLevel(level);
            }else{
                rawData = destinationMapper.findAllDestinations();
                cnt = destinationMapper.count();
            }
        }

        ret.setMessage("Successfully got all destinations");

        PaginationWrapper paginationWrapper = new PaginationWrapper();
        paginationWrapper.setData(rawData);
        paginationWrapper.setPageSize(size);
        paginationWrapper.setCurrentPage(page);
        paginationWrapper.setTotal(cnt);

        ret.setData(paginationWrapper);
        ret.setOk(true);

        return ret;
    }

    @GetMapping("/filter-short")
    public HichinaResponse getDestinationsFilterShort(@RequestParam(value = "query") String query){
        HichinaResponse ret = new HichinaResponse();

        List<DestinationShortDTO> rawData;
        if(!StringUtils.isEmpty(query)){
            rawData = destinationMapper.findDestinationShortByQuery(query);
        }else{
            ret.setOk(false);
            ret.setData(null);
            ret.setMessage("Do not accept empty query");
            return ret;
        }

        ret.setMessage("Successfully got all filtered destinations");

        ret.setData(rawData);
        ret.setOk(true);
        return ret;
    }
}
