package com.krugs.hackathon_1.service;

import com.krugs.hackathon_1.dto.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 지역별 관광지, 특산물, 테마 데이터 관리
 */
@Service
public class RegionDataService {

    private Map<String, RegionInfo> regionDatabase;

    public RegionDataService() {
        initializeRegionDatabase();
    }

    /**
     * 모든 지역 데이터 초기화
     */
    private void initializeRegionDatabase() {
        regionDatabase = new HashMap<>();

        // 대전 유성
        regionDatabase.put("대전 유성", RegionInfo.builder()
                .regionName("대전 유성")
                .centerLat(36.3742)
                .centerLng(127.3515)
                .attractions(List.of(
                        Attraction.builder().name("유성온천").latitude(36.3742).longitude(127.3515)
                                .category("온천").description("대전의 대표 온천 관광지").build(),
                        Attraction.builder().name("유성호").latitude(36.3750).longitude(127.3530)
                                .category("자연경관").description("물 맑은 유성호").build(),
                        Attraction.builder().name("대덕산").latitude(36.3800).longitude(127.3600)
                                .category("산").description("트레킹 명소").build(),
                        Attraction.builder().name("관광 호텔가").latitude(36.3720).longitude(127.3520)
                                .category("숙박").description("관광 호텔 밀집 지역").build()
                ))
                .specialties(List.of("유성 온천 계란", "대전 밤", "육포", "한우"))
                .themes(List.of("온천/휴양", "자연경관", "가족 관광"))
                .description("온천과 자연경관이 어우러진 관광지")
                .build());

        // 대전 도시 (엑스포, 기록관)
        regionDatabase.put("대전 도시", RegionInfo.builder()
                .regionName("대전 도시")
                .centerLat(36.3678)
                .centerLng(127.3071)
                .attractions(List.of(
                        Attraction.builder().name("엑스포 공원").latitude(36.3678).longitude(127.3071)
                                .category("공원").description("2023 대전 엑스포 개최지").build(),
                        Attraction.builder().name("대통령 기록관").latitude(36.3659).longitude(127.2979)
                                .category("문화").description("한국 대통령 역사 전시").build(),
                        Attraction.builder().name("대전 동물원").latitude(36.4295).longitude(127.3268)
                                .category("동물원").description("가족 친화적 동물원").build(),
                        Attraction.builder().name("한밭 광장").latitude(36.3270).longitude(127.4219)
                                .category("광장").description("대전의 중심 광장").build()
                ))
                .specialties(List.of("대전 김밥", "한우", "석탄샤베", "초콜릿"))
                .themes(List.of("도시관광", "문화체험", "현대건축"))
                .description("현대 문화시설과 도시의 중심")
                .build());

        // 공주 (역사문화)
        regionDatabase.put("공주", RegionInfo.builder()
                .regionName("공주")
                .centerLat(36.4564)
                .centerLng(127.1130)
                .attractions(List.of(
                        Attraction.builder().name("공주 국립박물관").latitude(36.4564).longitude(127.1130)
                                .category("박물관").description("백제 유물 전시").build(),
                        Attraction.builder().name("공산성").latitude(36.4580).longitude(127.1100)
                                .category("유산").description("백제의 산성").build(),
                        Attraction.builder().name("송산리 고분군").latitude(36.4500).longitude(127.1200)
                                .category("유산").description("백제 왕릉").build(),
                        Attraction.builder().name("금강").latitude(36.4600).longitude(127.1300)
                                .category("자연경관").description("충청의 젖줄").build()
                ))
                .specialties(List.of("공주 밤", "공주 복숭아", "계룡산 산채", "백제 쌀"))
                .themes(List.of("역사문화", "백제유산", "자연경관"))
                .description("백제 문화의 중심지")
                .build());

        // 부여 (백제문화)
        regionDatabase.put("부여", RegionInfo.builder()
                .regionName("부여")
                .centerLat(36.2833)
                .centerLng(126.9000)
                .attractions(List.of(
                        Attraction.builder().name("부여 국립박물관").latitude(36.2833).longitude(126.9000)
                                .category("박물관").description("백제 유물 및 예술").build(),
                        Attraction.builder().name("부소산성").latitude(36.2850).longitude(126.9020)
                                .category("유산").description("백제 마지막 성").build(),
                        Attraction.builder().name("백제 문화단지").latitude(36.2900).longitude(126.9100)
                                .category("문화").description("백제 문화 체험").build(),
                        Attraction.builder().name("궁남지").latitude(36.2950).longitude(126.9150)
                                .category("공원").description("백제 시대 저수지").build()
                ))
                .specialties(List.of("부여 딸기", "부여 오미자", "한우", "버들잎떡"))
                .themes(List.of("역사문화", "백제유산", "문화체험"))
                .description("백제 문화의 정수")
                .build());

        // 태안 (해변)
        regionDatabase.put("태안", RegionInfo.builder()
                .regionName("태안")
                .centerLat(36.9183)
                .centerLng(126.3000)
                .attractions(List.of(
                        Attraction.builder().name("태안 해변").latitude(36.9183).longitude(126.3000)
                                .category("해변").description("천연 해수욕장").build(),
                        Attraction.builder().name("신두리 사구").latitude(36.9200).longitude(126.2950)
                                .category("자연경관").description("해안 사막").build(),
                        Attraction.builder().name("안면도").latitude(36.9300).longitude(126.3200)
                                .category("섬").description("해안 관광지").build(),
                        Attraction.builder().name("태안 독조").latitude(36.9100).longitude(126.2900)
                                .category("해양").description("해양 생물").build()
                ))
                .specialties(List.of("태안 굴", "태안 새우젓", "태안 조개", "해산물"))
                .themes(List.of("해변", "자연경관", "해양관광"))
                .description("충남의 해양 관광 명소")
                .build());

        // 아산 (온천)
        regionDatabase.put("아산", RegionInfo.builder()
                .regionName("아산")
                .centerLat(36.7917)
                .centerLng(127.0075)
                .attractions(List.of(
                        Attraction.builder().name("아산 온천").latitude(36.7923).longitude(127.0089)
                                .category("온천").description("아산의 명물 온천").build(),
                        Attraction.builder().name("온천 관광지").latitude(36.7930).longitude(127.0100)
                                .category("관광").description("온천 호텔가").build(),
                        Attraction.builder().name("선문대학교").latitude(36.8000).longitude(127.0200)
                                .category("건축").description("캠퍼스 투어").build(),
                        Attraction.builder().name("아산만").latitude(36.8100).longitude(127.0300)
                                .category("자연경관").description("갯벌 생태").build()
                ))
                .specialties(List.of("아산 배", "아산 버섯", "한우", "간장게장"))
                .themes(List.of("온천/휴양", "미식관광", "자연경관"))
                .description("온천 휴양지")
                .build());

        // 천안 (도시)
        regionDatabase.put("천안", RegionInfo.builder()
                .regionName("천안")
                .centerLat(36.8143)
                .centerLng(127.1236)
                .attractions(List.of(
                        Attraction.builder().name("천안 삼거리").latitude(36.8143).longitude(127.1236)
                                .category("음식").description("천안 삼거리 쌀국수").build(),
                        Attraction.builder().name("독립기념관").latitude(36.8200).longitude(127.1300)
                                .category("박물관").description("한국 독립 역사").build(),
                        Attraction.builder().name("천안 봉면").latitude(36.8100).longitude(127.1100)
                                .category("자연").description("봉면 계곡").build(),
                        Attraction.builder().name("광대산").latitude(36.8300).longitude(127.1400)
                                .category("산").description("등산로").build()
                ))
                .specialties(List.of("천안 삼거리 쌀국수", "천안 호두과자", "배", "한우"))
                .themes(List.of("도시관광", "역사문화", "미식관광"))
                .description("충남의 교통 중심지")
                .build());

        // 해미 (읍성)
        regionDatabase.put("해미", RegionInfo.builder()
                .regionName("해미")
                .centerLat(36.5750)
                .centerLng(126.5833)
                .attractions(List.of(
                        Attraction.builder().name("해미 읍성").latitude(36.5750).longitude(126.5833)
                                .category("유산").description("조선시대 읍성").build(),
                        Attraction.builder().name("해미 순교지").latitude(36.5760).longitude(126.5850)
                                .category("종교").description("천주교 순교지").build(),
                        Attraction.builder().name("해미 만").latitude(36.5800).longitude(126.5900)
                                .category("자연경관").description("갯벌").build(),
                        Attraction.builder().name("용당 마을").latitude(36.5700).longitude(126.5700)
                                .category("민속").description("전통 마을").build()
                ))
                .specialties(List.of("해미 굴", "새우젓", "멸치", "조개"))
                .themes(List.of("역사문화", "종교문화", "해양관광"))
                .description("조선시대 문화와 천주교 역사")
                .build());

        // 계룡산 (산악)
        regionDatabase.put("계룡산", RegionInfo.builder()
                .regionName("계룡산")
                .centerLat(36.3095)
                .centerLng(127.1433)
                .attractions(List.of(
                        Attraction.builder().name("계룡산").latitude(36.3095).longitude(127.1433)
                                .category("산").description("국립공원").build(),
                        Attraction.builder().name("동학사").latitude(36.3100).longitude(127.1450)
                                .category("종교").description("계룡산 사찰").build(),
                        Attraction.builder().name("신계사").latitude(36.3150).longitude(127.1400)
                                .category("종교").description("역사 사찰").build(),
                        Attraction.builder().name("계룡산 트레킹로").latitude(36.3200).longitude(127.1500)
                                .category("산").description("등산로").build()
                ))
                .specialties(List.of("산채", "더덕", "도라지", "산 나물"))
                .themes(List.of("산악", "종교문화", "자연경관"))
                .description("충남의 영산")
                .build());
    }

    /**
     * 시작 위치 기반으로 가장 가까운 지역 정보 반환
     */
    public RegionInfo getRegionByStartLocation(String startLocation) {
        // 정확한 지역명이 입력된 경우
        if (regionDatabase.containsKey(startLocation)) {
            return regionDatabase.get(startLocation);
        }

        // 부분 일치 (예: "대전" 입력 시 "대전 유성" 또는 "대전 도시" 반환)
        for (String key : regionDatabase.keySet()) {
            if (key.contains(startLocation) || startLocation.contains(key.split(" ")[0])) {
                return regionDatabase.get(key);
            }
        }

        // 기본값: 대전 유성
        return regionDatabase.get("대전 유성");
    }

    /**
     * 모든 등록된 지역 반환
     */
    public Collection<RegionInfo> getAllRegions() {
        return regionDatabase.values();
    }

    /**
     * 특정 테마의 지역 목록 반환
     */
    public List<RegionInfo> getRegionsByTheme(String theme) {
        List<RegionInfo> results = new ArrayList<>();
        for (RegionInfo region : regionDatabase.values()) {
            if (region.getThemes().stream().anyMatch(t -> t.contains(theme))) {
                results.add(region);
            }
        }
        return results;
    }
}

