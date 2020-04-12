package cn.pigicutils.core.tree.bean;

/**
 * @author guchang.pan@hand-china.com
 * @Date: 2019/10/11 8:38
 */

import java.util.LinkedList;
import java.util.List;

public class NodeInfo<T extends Nodeable> implements Cloneable{

    static String rootId = "@ROOT";   //根节点id
    static String rootParentId = "";   //根节点的parent
    static int rootLevel = 0;   //根节点的级别


    /**
     * 默认根节点
     * */
    public static NodeInfo def = new NodeInfo();
    static  {
        def.cont = new Nodeable() {
            @Override
            public String theId() {
                return rootId;
            }

            @Override
            public String theParentId() {
                return rootParentId;
            }
        };
        def.level = rootLevel;
        def.id = rootId;
        def.parentId = rootParentId;
        def.parentIds = rootParentId;
        def.children = new LinkedList<>();
    };


    /**
     * 当前元素
     * */
    private T cont;
    private String id;        //当前元素id
    private String parentId;  //当前元素父节点id
    private List<NodeInfo<T>> children;   //所有的下级节点


    private int level;  //从根节点开始计算，第n级节点
    private String parentIds;
    private String slipt = ",";  //节点所有parentIds的分割符



    NodeInfo(T cont) throws Exception{
        if(cont == null || TreeHandler.isBlank(cont.theId())){
            throw new Exception("元素或者元素id不能为空");
        }
        this.cont = cont;
        this.id = TreeHandler.trimToEmpty(cont.theId());
        this.parentId = TreeHandler.trimToEmpty(cont.theParentId());
    }
    private NodeInfo(){}



    @Override
    public NodeInfo<T> clone(){
        try { return (NodeInfo<T>) super.clone(); } catch ( CloneNotSupportedException e ) { }
        return null;
    }

    public T getCont() {
        return cont;
    }


    public NodeInfo<T> putParent(NodeInfo<T> parent, int level, NodeInfo<T> root){
        this.level = level;
        this.parentId = parent.id;
        this.parentIds = (parent.getParentIds() + root.slipt + parent.getId()).replaceFirst("^" + root.slipt, "");
        return this;
    }
    public void putChild(NodeInfo<T> child){
        if( children == null ){
            children = new LinkedList<>();
        }
        children.add(child);
    }


    /**
     * 设置分割符
     * */
    public void setSlipt(String slipt) {
        this.slipt = slipt ;
    }
    String getSlipt() {
        return slipt;
    }

    public void setCont(T cont) {
        this.cont = cont;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public List<NodeInfo<T>> getChildren() {
        return children;
    }

    public void setChildren(List<NodeInfo<T>> children) {
        this.children = children;
    }
}


