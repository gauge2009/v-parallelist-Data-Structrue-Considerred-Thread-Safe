package Stack;

public class LinkNode {
	private int key = 0;
    private String value = null;
    private LinkNode next = null;
   
    public LinkNode(int key, String data){
        this.key = key;
        this.value = data;
        this.next = null;
    }
   
    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }


    public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LinkNode getNext() {
        return next;
    }

    public void setNext(LinkNode next) {
        this.next = next;
    }
}