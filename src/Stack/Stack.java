package Stack;

public interface Stack<T> {
	/**
	 * ��Ԫ��ѹ��ջ��
	 * @param string
	 */
	public void push(LinkNode string);
	/**
	 * ����ջ��Ԫ��
	 */
	public LinkNode pop();
	/**
	 * ��ӡ��ǰջ������Ԫ��
	 */
	public void printStack();
	/**
	 * ���ص�ǰջ��Ԫ�ظ���
	 * @return
	 */
	public int length();
}