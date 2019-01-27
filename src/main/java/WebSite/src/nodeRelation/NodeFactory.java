package nodeRelation;


import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeFactory {


    public static StringBuffer getNode(List<String>urls,List<String>refs){
        StringBuffer buffer=new StringBuffer();
        List<Integer>father=new ArrayList<>();
        List<Integer>all=new ArrayList<>();
        int len=urls.size();
        for (int i = 0; i < len; i++) {
            all.add(i);
        }

        buffer.append("主要事件");
        for (int i = 0; i < len; i++) {
            if(!urls.contains(refs.get(i))){
                father.add(i);
                all.remove((Object)i);
                buffer.append(urls.get(i)+"\n");
            }
        }
        buffer.append("\n\n爆发事件:");

        for (int i = 0; i < father.size(); i++) {
            String str=urls.get(father.get(i));
            for (int j = 0; j < refs.size(); j++) {
                if(!all.contains(j)){
                    continue;
                }
                if(refs.contains(str)){
                    buffer.append(urls.get(refs.indexOf(str)));
                }
            }
            buffer.append("\n");
        }
        return buffer;
    }
    public void toChildren(){


    }
}
