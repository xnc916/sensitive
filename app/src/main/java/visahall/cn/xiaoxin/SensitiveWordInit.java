package visahall.cn.xiaoxin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Moya on 2017/11/28.
 * @Description: 初始化敏感词库，将敏感词加入到HashMap中，构建DFA算法模型
 */
public class SensitiveWordInit {
    private String ENCODING = "UTF-8";    //字符编码
    @SuppressWarnings("rawtypes")
    public HashMap sensitiveWordMap;

    public SensitiveWordInit(){
        super();
    }


    @SuppressWarnings("rawtypes")
    public Map initKeyWord(){
        try {
            //读取敏感词库
            Set<String> keyWordSet = new HashSet<String>();;
            keyWordSet.add("蜂蜜");
            keyWordSet.add("维尼熊");
            keyWordSet.add("木头");
            keyWordSet.add("洗澡");
            keyWordSet.add("123456");
            keyWordSet.add("abc");
            keyWordSet.add("森林");
            //将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
            //spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
     * 中 = {
     *      isEnd = 0
     *      国 = {<br>
     *      	 isEnd = 1
     *           人 = {isEnd = 0
     *                民 = {isEnd = 1}
     *                }
     *           男  = {
     *           	   isEnd = 0
     *           		人 = {
     *           			 isEnd = 1
     *           			}
     *           	}
     *           }
     *      }
     *  五 = {
     *      isEnd = 0
     *      星 = {
     *      	isEnd = 0
     *      	红 = {
     *              isEnd = 0
     *              旗 = {
     *                   isEnd = 1
     *                  }
     *              }
     *      	}
     *      }
     * @author DUDUfine
     * @param keyWordSet  敏感词库
     * @version 1.0
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while(iterator.hasNext()){
            key = iterator.next();    //关键字
            nowMap = sensitiveWordMap;
            for(int i = 0 ; i < key.length() ; i++){
                char keyChar = key.charAt(i);      //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取

                if(wordMap != null){        //如果存在该key，直接赋值ֵ
                    nowMap = (Map) wordMap;
                }
                else{     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String,String>();
                    newWorMap.put("isEnd", "0");    //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if(i == key.length() - 1){
                    nowMap.put("isEnd", "1");   //最后一个
                }
            }
        }
    }

    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     * @throws Exception
     */
    @SuppressWarnings("resource")
    private Set<String> readSensitiveWordFile() throws Exception{
        Set<String> set = null;
        //File file = new File("C:/1/12.txt");//读取文件,绝对路径

        File file = new File(SensitiveWordInit.class.getClassLoader().getResource("12.txt").getPath());//读取文件,相对路径
        InputStreamReader read = new InputStreamReader(new FileInputStream(file),ENCODING);
        try {
            if(file.isFile() && file.exists()){      //文件流是否存在
                set = new HashSet<String>();
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt = null;
                while((txt = bufferedReader.readLine()) != null){    //读取文件，将文件内容放入到set中
                    set.add(txt);
                }
            }
            else{         //不存在抛出异常信息
                throw new Exception("敏感词库文件不存在");
            }
        } catch (Exception e) {
            throw e;
        }finally{
            read.close();    //关闭文件流
        }
        return set;
    }
}
