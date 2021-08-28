package com.chanctn.jhipster_sample_application.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.chanctn.jhipster_sample_application.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PromotionInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromotionInfo.class);
        PromotionInfo promotionInfo1 = new PromotionInfo();
        promotionInfo1.setId(1L);
        PromotionInfo promotionInfo2 = new PromotionInfo();
        promotionInfo2.setId(promotionInfo1.getId());
        assertThat(promotionInfo1).isEqualTo(promotionInfo2);
        promotionInfo2.setId(2L);
        assertThat(promotionInfo1).isNotEqualTo(promotionInfo2);
        promotionInfo1.setId(null);
        assertThat(promotionInfo1).isNotEqualTo(promotionInfo2);
    }
}
