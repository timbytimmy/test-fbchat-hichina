package com.hichina.admin.hichinaadminbackend.schedule;

import com.hichina.admin.hichinaadminbackend.controller.ImageController;
import com.hichina.admin.hichinaadminbackend.mapper.BlogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class ScheduledJobs {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledJobs.class);

    @Autowired
    private BlogMapper blogMapper;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void UpdateBlogProfileImage(){
        LOG.info("===executing update blog profile image");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, -1  );
        blogMapper.updateCoverWithFirstImage(calendar.getTime());
    }

}
