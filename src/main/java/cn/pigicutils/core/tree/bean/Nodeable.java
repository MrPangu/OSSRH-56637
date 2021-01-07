package cn.pigicutils.core.tree.bean;

/**
 * 树节点对象，必须实现这个interface，并且实现theId、theParentId方法！
 * 分别返回节点的id，和父节点id
 * @author guchang.pan@hand-china.com
 *
 */
public interface Nodeable {
    /**
     * 返回当前节点id
     * */
    public String theId();
    /**
     * 返回当前节点父节点id
     * */
    public String theParentId();
}

