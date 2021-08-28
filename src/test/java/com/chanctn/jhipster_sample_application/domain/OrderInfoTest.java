package com.chanctn.jhipster_sample_application.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.chanctn.jhipster_sample_application.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderInfo.class);
        OrderInfo orderInfo1 = new OrderInfo();
        orderInfo1.setId(1L);
        OrderInfo orderInfo2 = new OrderInfo();
        orderInfo2.setId(orderInfo1.getId());
        assertThat(orderInfo1).isEqualTo(orderInfo2);
        orderInfo2.setId(2L);
        assertThat(orderInfo1).isNotEqualTo(orderInfo2);
        orderInfo1.setId(null);
        assertThat(orderInfo1).isNotEqualTo(orderInfo2);
    }
}
