package com.acme.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@ComponentScan
@EnableAutoConfiguration
@Controller
@RequestMapping(produces="application/json")
public class BreakController {

    private TreeSet<String> breakSet = new TreeSet<String>();
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm");
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm");

    
    @RequestMapping("/break")
    public @ResponseBody TreeSet<String> list() {
	    return breakSet;
    }
    
    @RequestMapping(value="/break/{time}", method=RequestMethod.POST)
    public @ResponseBody String add(@PathVariable String time) {
        breakSet.add(time);
        return "{\"status\": \"added\"}";
    }

    @RequestMapping(value="/break/{time}", method=RequestMethod.DELETE)
    public @ResponseBody String remove(@PathVariable String time) {
        breakSet.remove(time);
        return "{\"status\": \"removed\"}";
    }

    @RequestMapping("/minutes")
    public @ResponseBody String minutes() throws ParseException {
        long minutes = -1;
        Date now = new Date();
        String today = dateFormatter.format(now);
        String hours = hourFormatter.format(now);
        String nextBreak = breakSet.higher(hours);
        if (nextBreak != null) {
            Date breakTime = sdf.parse(today + nextBreak);
            minutes = (int) ((breakTime.getTime() - now.getTime()) / 60000);
        }
        return "{\"minutes\" : " + minutes + "}";
    }
    public static void main(String[] args) {
        SpringApplication.run(BreakController.class, args);
    }
}