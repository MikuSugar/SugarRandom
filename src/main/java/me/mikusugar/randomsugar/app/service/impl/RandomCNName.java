package me.mikusugar.randomsugar.app.service.impl;

import me.mikusugar.randomsugar.app.bean.SugarJsonNode;
import me.mikusugar.randomsugar.app.constant.ServiceName;
import me.mikusugar.randomsugar.app.service.AbstractRandomService;
import me.mikusugar.randomsugar.app.utils.RandomUtilInterface;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service(ServiceName.RANDOM_CN_NAME)
public class RandomCNName extends AbstractRandomService<String> {
  @Override
  protected RandomUtilInterface<String> createRandomUtilInterface(String input) {

    if(input.isEmpty()){
      return () -> {
        if(random.nextInt()%2==0) return getRandName(preName)+getRandomBoyLastName();
        else return getRandName(preName)+getRandomGirlLastName();
      };
    }
    else {
      final int num = Integer.parseInt(input);
      if(num==0){
        return () -> getRandName(preName)+getRandomGirlLastName();
      }
      else {
        return () -> getRandName(preName)+getRandomBoyLastName();
      }
    }

  }

  private final static Random random = new Random();

  private static String getRandomGirlLastName(){
    final String pre=getRandName(girlName);
    if(random.nextInt()%2==0)return pre+getRandName(girlName);
    return pre;
  }

  private static String getRandomBoyLastName(){
    final String pre=getRandName(boyName);
    if(random.nextInt()%2==0)return pre+getRandName(boyName);
    return pre;
  }

  private static String getRandName(String str){
    final int idx = (int) (Math.random() * str.length());
    return str.substring(idx,idx+1);
  }

  private static final String preName =
      "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华"
          + "金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪"
          + "汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞"
          + "熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡凌霍虞万支柯咎管卢莫经房裘缪干解应宗宣丁贲邓"
          + "郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄魏加封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁"
          + "仇栾暴甘钭厉戎祖武符刘姜詹束龙叶幸司韶郜黎蓟薄印宿白怀蒲台从鄂索咸籍赖卓蔺屠蒙池乔阴郁胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉"
          + "宰郦雍却璩桑桂濮牛寿通边扈燕冀郏浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庚终暨居衡步都耿满弘匡国文寇广禄阙东殴殳"
          + "沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查后江红游竺权逯盖益桓公万俟司马上官欧阳夏侯诸葛闻人"
          + "东方赫连皇甫尉迟公羊澹台公冶宗政濮阳淳于仲孙太叔申屠公孙乐正轩辕令狐钟离闾丘长孙慕容鲜于宇文司徒司空亓官司寇仉督子车颛孙"
          + "端木巫马公西漆雕乐正壤驷公良拓拔夹谷宰父谷粱晋楚阎法汝鄢涂钦段干百里东郭南门呼延归海羊舌微生岳帅缑亢况后有琴梁丘左丘东门西门"
          + "商牟佘佴伯赏南宫墨哈谯笪年爱阳佟";

  private static final String girlName =
      "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶"
          + "妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣"
          + "飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽";


  private final static String boyName = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元" +
          "全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河" +
          "哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";


  @Override
  public String helpText() {
    return "输入0随机生成女性名字，输入1随机生成男性名字，不输入都生成";
  }

  @Override
  public boolean check(String type, String input) {
    if (!SugarJsonNode.TYPE.STRING.toString().equals(type)) {
      return false;
    }
    if (input.trim().isEmpty()) return true;
    int num = Integer.parseInt(input);
    return num == 0 || num == 1;
  }
}
