package Stack;

public interface Stack<T> {
	/**
	 * 将元素压入栈中
	 * @param string
	 */
	public void push(LinkNode string);
	/**
	 * 弹出栈顶元素
	 */
	public LinkNode pop();
	/**
	 * 打印当前栈中所有元素
	 */
	public void printStack();
	/**
	 * 返回当前栈中元素个数
	 * @return
	 */
	public int length();
}