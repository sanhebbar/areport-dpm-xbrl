package com.areport_dpm_xbrl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigTest {
    private Config sut;

    @Test
    public void t1(){
        assertEquals("path/to/storage/app/public/tax/" ,Config.publicDir());
    }
    @Test
    public void t2(){
        assertEquals("path/to/public/images/logo.svg",Config.setLogoPath());
    }
    @Test
    public void t3(){
        assertEquals(3,Config.owners().size());
    }
}
