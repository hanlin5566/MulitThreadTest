// package com.hl.thread;
// import com.google.common.collect.Lists;
// import java.util.Date;
// import java.util.List;
// import java.util.concurrent.ArrayBlockingQueue;
// import java.util.concurrent.CountDownLatch;
// import java.util.concurrent.ThreadPoolExecutor;
// import java.util.concurrent.TimeUnit;
// import java.util.stream.IntStream;
// import org.junit.Test;
//
// /**
//  * @author Hanson
//  * @date 2020/7/917:12
//  */
// public class InnerClassTest {
//
//     @Test
//     public void staticInnerClass() throws InterruptedException {
//         //单线程赋值创建
//         signleThread();
//         //多线程
//         mutileThreadCreate();
//     }
//
//     private void signleThread() {
//         ClueDetailResponseBo bo = ClueDetailResponseBo.builder().build();
//         ClueDetailResponseBo.IdentityBo identityBo1 = ClueDetailResponseBo.IdentityBo.builder().idCard("1").phone("123").realName("hu123").build();
//         ClueDetailResponseBo.IdentityBo identityBo2 = ClueDetailResponseBo.IdentityBo.builder().idCard("2").phone("234").realName("hu234").build();
//         ClueDetailResponseBo.QualificationApplyBo qualificationApplyBo1 = ClueDetailResponseBo.QualificationApplyBo.builder().requestId("1").build();
//         ClueDetailResponseBo.QualificationApplyBo qualificationApplyBo2 = ClueDetailResponseBo.QualificationApplyBo.builder().requestId("1").build();
//         ClueDetailResponseBo.RecommendStepBO recommendStepBO1 = ClueDetailResponseBo.RecommendStepBO.builder().answerKey("a1").questionKey("q1").build();
//         ClueDetailResponseBo.RecommendStepBO recommendStepBO2 = ClueDetailResponseBo.RecommendStepBO.builder().answerKey("a1").questionKey("q1").build();
//
//         //赋值没问题
//         identityBo2.setIdCard("3");
//         identityBo1.setPhone("456");
//
//         identityBo1.setRealName("hanson");
//
//         System.out.println(identityBo1);
//         System.out.println(identityBo2);
//     }
//
//     private void mutileThreadCreate() throws InterruptedException {
//         List<ClueDetailResponseBo.IdentityBo> identityBoList = Lists.newArrayList();
//         List<ClueDetailResponseBo.QualificationApplyBo> qualificationApplyBoList = Lists.newArrayList();
//         List<ClueDetailResponseBo.RecommendStepBO> recommendStepBOList = Lists.newArrayList();
//         ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10,20,10, TimeUnit.MINUTES,new ArrayBlockingQueue<>(100));
//
//         //多线程赋值
//         int max = 20;
//         CountDownLatch countDownLatch = new CountDownLatch(max);
//
//         IntStream.rangeClosed(0,max).forEach(i->{
//             poolExecutor.submit(new Runnable() {
//                 @Override
//                 public void run() {
//                     identityBoList.add(ClueDetailResponseBo.IdentityBo.builder().idCard(i+"_idcar").phone(i+"_phone").realName(i+"_name").build());
//                     qualificationApplyBoList.add(ClueDetailResponseBo.QualificationApplyBo.builder().requestId("request"+i).build());
//                     recommendStepBOList.add(ClueDetailResponseBo.RecommendStepBO.builder().answerKey("a"+i).questionKey("q"+i).build());
//                     countDownLatch.countDown();
//                 }
//             });
//         });
//         countDownLatch.await(2,TimeUnit.SECONDS);
//
//         System.err.println(JSONUtil.toJSONString(identityBoList));
//         System.err.println(JSONUtil.toJSONString(qualificationApplyBoList));
//         System.err.println(JSONUtil.toJSONString(recommendStepBOList));
//     }
//
//     private ClueDetailResponseBo.IdentityBo convertUserIdentityPoToBo(UserIdentityPo po) {
//         return ClueDetailResponseBo.IdentityBo.builder()
//                 .idCard(MaskUtil.maskSsn(po.getIdCardNo()))
//                 .realName(po.getName())
//                 //目前都是三要素认证,所以默认是三要素
//                 .identityType(IdentityType.THREE_ELEMENTS)
//                 .phone(MaskUtil.maskPhone(po.getPhone()))
//                 .build();
//     }
//
//     private ClueDetailResponseBo.QualificationApplyBo convertProductRecommendRequestPOToBo(ProductRecommendRequestPO po) {
//         ClueDetailResponseBo.QualificationApplyBo bo = ClueDetailResponseBo.QualificationApplyBo.builder()
//                 .requestId(po.getRequestId())
//                 .products(JSONUtil.parseList(po.getRecommendProducts(), RecommendProductBO.class))
//                 .recommendSteps(JSONUtil.parseList(po.getQaRecords(), ClueDetailResponseBo.RecommendStepBO.class))
//                 .build();
//         //填充问题文本和答案文本
//         bo.getRecommendSteps().stream().forEach(itm -> {
//             QuestionEnum questionEnum = QuestionEnum.keyOf(itm.getQuestionKey());
//             itm.setQuestionText(questionEnum.getQuestionText());
//             itm.setAnswerText(questionEnum.getAnswerText(itm.getAnswerKey()));
//         });
//         return bo;
//     }
//
//     @Data
//     @Builder
//     @NoArgsConstructor
//     @AllArgsConstructor
//     public static class  ClueDetailResponseBo {
//         @ApiModelProperty(value = "线索实名信息")
//         private List<IdentityBo> identities;
//
//         @ApiModelProperty(value = "资质预审信息")
//         private List<QualificationApplyBo> qualificationApplies;
//
//         @ApiModelProperty(value = "分配记录")
//         private UserClueAdvisorLogBo userClueAdvisorLogBo;
//
//         @Data
//         @Builder
//         @NoArgsConstructor
//         @AllArgsConstructor
//         public static class IdentityBo{
//             @ApiModelProperty(value = "资质审核id")
//             private Long applyId;
//
//             @ApiModelProperty(value = "实名认证类型")
//             private Integer identityType;
//
//             @ApiModelProperty(value = "实名认证状态")
//             private Integer identityStatus;
//
//             @ApiModelProperty(value = "用户姓名")
//             private String realName;
//
//             @ApiModelProperty(value = "身份证号")
//             private String idCard;
//
//             @ApiModelProperty(value = "电话号码")
//             private String phone;
//         }
//
//         @Data
//         @Builder
//         @NoArgsConstructor
//         @AllArgsConstructor
//         public static class QualificationApplyBo{
//             @ApiModelProperty(value = "资质预审的唯一标识")
//             private Long applyId;
//
//             @ApiModelProperty(value = "推荐问答的唯一标识")
//             private String requestId;
//
//             @ApiModelProperty(value = "资质预审申请状态")
//             private QualificationApplyStatus applyStatus;
//
//             @ApiModelProperty(value = "预审更新时间")
//             private Date updateDate;
//
//             @ApiModelProperty(value = "推荐步骤")
//             private List<RecommendStepBO> recommendSteps;
//
//             @ApiModelProperty(value = "推荐产品")
//             private List<RecommendProductBO> products;
//         }
//
//         @Data
//         @Builder
//         @NoArgsConstructor
//         @AllArgsConstructor
//         public static class RecommendStepBO{
//             @ApiModelProperty(value = "问答序号")
//             private Integer seq;
//
//             @ApiModelProperty(value = "问题标识")
//             private String questionKey;
//
//             @ApiModelProperty(value = "问题描述")
//             private String questionText;
//
//             @ApiModelProperty(value = "答案标识")
//             private String answerKey;
//
//             @ApiModelProperty(value = "答案描述")
//             private String answerText;
//         }
//     }
// }
