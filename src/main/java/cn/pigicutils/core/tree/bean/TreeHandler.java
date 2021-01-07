package cn.pigicutils.core.tree.bean;

/**
 * @author guchang.pan@hand-china.com
 *
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TreeHandler<T extends Nodeable> {


    /****************************************************/

    private Map<String, NodeInfo<T>> nodeMap;



    /**
     * 根节点
     * */
    private NodeInfo<T> root;



    TreeHandler(){}


    /**
     * @param root 根节点对象
     * */
    public TreeHandler(NodeInfo<T> root){
        this.root = root;
        this.root.setId(root.getId());
        this.root.setParentId(root.getParentId());
        this.root.setParentIds(root.getParentIds());
        this.root.setSlipt(root.getSlipt());
    }

    /**
     * @param element   根节点元素
     * @param rootParentId  根节点的上级id
     * @param slipt     元素所有上级id分割符
     */
    public TreeHandler(T element, String rootParentId, String slipt) throws Exception{
        this.root = creatNode(element);
        this.root.setId(element.theId());
        this.root.setParentId(element.theParentId());
        this.root.setParentIds(rootParentId);
        this.root.setSlipt(slipt);
    }

    /**
     * @param rootId 根节点的id，注意，这样会在nodeMap中存入一个虚拟的Nodeable节点，
     *  在后续遍历nodes时，会可能造成类型转换异常
     * */
    public TreeHandler(String rootId){
        this(rootId, ",");
    }

    /**
     * @param rootId 根节点的id，注意，这样会在nodeMap中存入一个虚拟的Nodeable节点，
     *  在后续遍历nodes时，会可能造成类型转换异常
     * @param slipt 元素所有上级id分割符，默认","
     * */
    public TreeHandler(String rootId, String slipt){
        this(rootId, "", slipt);
    }

    /**
     * @param rootId 根节点的id
     * @param rootParentId 根节点的上级id，默认""
     * @param slipt 元素所有上级id分割符，默认","
     * */
    public TreeHandler(String rootId, String rootParentId, String slipt){
        this.root = NodeInfo.def.clone();
        this.root.setId(rootId);
        this.root.setParentId(rootParentId);
        this.root.setParentIds(rootParentId);
        this.root.setSlipt(slipt);
    }


    /**
     * 解析，将扁平数据解析成树状
     * @param elements 待处理集合
     * */
    public NodeInfo<T> parseArray(List<T> elements) throws Exception{
        if( TreeHandler.isNotEmpty(elements) ){
            nodeMap = new HashMap<>((int)(elements.size() * 1.34));
            nodeMap.put(root.getId(), root);
            Map<String, List<NodeInfo<T>>> parentIdMap = this.groupByParentId(elements);    //按parentId分组后的数据
            List<NodeInfo<T>> firsts = parentIdMap.get(root.getId());
            root.setChildren(firsts);
            for( NodeInfo<T> first : firsts){
                first.putParent(root, root.getLevel(), root);
                parse(first, root, root.getLevel(), parentIdMap);
            }
        }
        return root;
    }


    /**
     * 解析
     * @param curNode 当前节点
     * @param parentNode 上级节点
     * @param level 当前节点所在级别
     * */
    private void parse(NodeInfo<T> curNode, NodeInfo<T> parentNode, int level, Map<String, List<NodeInfo<T>>> parentIdMap){

        level ++ ;

        List<NodeInfo<T>> curNodeChildren = parentIdMap.get(curNode.getId());   //得到当前节点的子节点

        curNode.putParent(parentNode, parentNode.getLevel(), root);
        curNode.setChildren(curNodeChildren);
        curNode.setLevel(level);

        if( TreeHandler.isEmpty(curNodeChildren) ){//当前节点没有子节点了
            return;
        }
        //当前节点有子节点
        for( NodeInfo<T> child : curNodeChildren ){
            parse(child, curNode, level, parentIdMap);
        }
        return;
    }






    /**
     * 根据节点id，从树上，剪掉此节点、以及其后代节点
     * */
    public NodeInfo<T> cutNodeById(String nodeId){
        NodeInfo<T> cutNode = queryTree(nodeId);
        if( cutNode != null ){
            NodeInfo<T> parentNode = queryTree(cutNode.getParentId());
            cutNode(cutNode);
            List<NodeInfo<T>> children = parentNode.getChildren();
            int i = 0;
            for(NodeInfo<T> child : children){
                if( cutNode.getId().equals(child.getId()) ){
                    break;
                }
                i ++ ;
            }
            children.remove(i);
        }
        return cutNode;
    };


    /**
     * 根据节点id，分离成一个子树
     * */
    public TreeHandler splitById(String nodeId, String rootParentId) throws Exception {
        return splitById(nodeId, rootParentId, ",");
    }
    public TreeHandler splitById(String nodeId, String rootParentId, String slipt) throws Exception {
        TreeHandler handler = new TreeHandler();

        if( nodeMap.containsKey(nodeId) ){
            NodeInfo<T> tree = cutNodeById(nodeId);
            handler.root = tree;
            handler.root.setId(tree.getId());
            handler.root.setParentId(tree.getParentId());
            handler.root.setParentIds(rootParentId);
            handler.root.setSlipt(slipt);

            List<T> elements = new LinkedList<>();
            getNodes(tree, elements);

            handler.root.getChildren().clear();
            handler.parseArray(elements);

        }
        return handler;
    };

    private void getNodes(NodeInfo<T> node, List<T> elements){
        List<NodeInfo<T>> children = node.getChildren();
        if( TreeHandler.isEmpty(children) ){
            elements.add(node.getCont());
            return;
        }
        for ( NodeInfo<T> child : children ){
            getNodes(child, elements);
        }
        elements.add(node.getCont());
    }



    /**
     * @param node 枝剪某个节点
     * */
    private void cutNode(NodeInfo<T> node){
        List<NodeInfo<T>> children = node.getChildren();
        if( TreeHandler.isEmpty(children) ){//如果没有子节点了
            nodeMap.remove(node.getId());
            return;
        }
        for(NodeInfo<T> child : children){
            cutNode(child);
        }
        nodeMap.remove(node.getId());
    }





    /**
     * 得到所有的节点
     * */
    public List<NodeInfo<T>> getNodes(){
        Collection<NodeInfo<T>> values = nodeMap.values();
        List<NodeInfo<T>> list = new ArrayList<>(values.size());
        for( NodeInfo<T> o : values ){
            list.add(o);
        }
        return list;
    }

    /**
     * 得到Map： 节点id -> 节点
     * */
    public Map<String, NodeInfo<T>> getNodeMap(){
        return nodeMap;
    }

    /**
     * 创建节点
     * */
    public NodeInfo creatNode(T element) throws Exception {
        NodeInfo<T> node = null;
        if( nodeMap != null ){
            node = nodeMap.get(element.theId());
        }
        if( node == null ){
            node = new NodeInfo(element);
        }
        //如果元素没有传入上级节点，按一级节点
        if( TreeHandler.isBlank(node.getParentId()) ){
            node.setParentId(root.getId());
        }
        return node;
    }



    /**
     * 遍历树，并且返回该节点
     * */
    public NodeInfo<T> queryTree(String nodeId){
        NodeInfo<T> node = nodeMap.get(nodeId);
        return node;
    }



    /**
     * 遍历，找到上级节点
     * */
    private NodeInfo<T> foreach(List<NodeInfo<T>> children, String seachId){
        NodeInfo<T> node = null;
        if( TreeHandler.isNotEmpty(children) ){
            for( NodeInfo child : children ){
                System.out.println(child.getId());
                if( seachId.equals(child.getId()) ){
                    return child;
                }
                node = foreach(child.getChildren(), seachId);
                if(node != null){
                    return node;
                }
            }
            return null;
        }else{
            return null;
        }
    }




    private Map<String, List<NodeInfo<T>>> groupByParentId(List<T> elements) throws Exception{
        Map<String, List<NodeInfo<T>>> result = new HashMap<>((int)(elements.size() * 1.34));
        for( T el : elements ){
            String parent = TreeHandler.defaultIfBlank(el.theParentId(), root.getId());
            if( parent.equals(el.theId()) ){
                throw new Exception("元素id和parentId不能相等[id='" + el.theId() + "']");
            }
            List<NodeInfo<T>> group = result.get(parent);
            if ( group == null ){
                group = new LinkedList<>();
                result.put(parent, group);
            }
            NodeInfo<T> node = this.creatNode(el);
            group.add(node);
            nodeMap.put(el.theId(), node);
        }
        return result;
    }




    public static boolean isBlank (String string) {
        return string == null || "".equals(string.trim());
    }

    public static String trimToEmpty (String string) {
        return isBlank(string) ? "" : string.trim();
    }
    private static String defaultIfBlank (String string, String defaultValue) {
        return isBlank(string) ? defaultValue : string;
    }
    public static boolean isEmpty (List<?> list) {
        return list == null || list.isEmpty();
    }
    public static boolean isNotEmpty (List<?> list) {
        return !isEmpty(list);
    }
}
