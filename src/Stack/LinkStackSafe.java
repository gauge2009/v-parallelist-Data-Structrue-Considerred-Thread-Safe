package Stack;

public class LinkStackSafe {

	public LinkNode top = null;
	private int length = 0;

	public LinkStackSafe() {
		top = new LinkNode(0, "null");
	}

	public  synchronized LinkNode pop() {
		top=top.getNext();
		length--;
		return top;
	}
  ///����һ�� ����synchronized  ���������������ʱ�򣬻��ǳ��ֲ�ͬ��
	public  synchronized  void  push(LinkNode  ln) {
		if(ln!=null) {
			ln.setNext(top);
			top=ln;
			length++;
		}
	}

	public int length() {
		return this.length;
	}

	public void printStack() {
		LinkNode curnode = top;
		if (curnode != null) {
			System.out.println("---------");
			while (curnode != null) {
				System.out.println("key=" + curnode.getKey() + "--name" + curnode.getValue());
				curnode = curnode.getNext();
			}
			System.out.println("---------");
		} else {
			System.out.println("stack is Empty");
		}
	}

	// ���̲߳���һ��ջ

	public static void main(String[] args) throws InterruptedException {
		LinkStackSafe mystack = new LinkStackSafe();
		int N = 50;
		Thread[] ths = new Thread[N];
		for (int i = 0; i < N; i++) {
			int temp = i;
//			Thread th = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					mystack.push(new LinkNode(temp, "A" + temp));
//				}
//			});
			Thread th = new Thread(()->{
				mystack.push(new LinkNode(temp, "A" + temp));
			});
			th.start();
			ths[temp] = th;
		}
		for (int i = 0; i < N; i++) {
			ths[i].join();
		}
		//����ȱʧ��
		for (int i = 0; i < N; i++) {
			LinkNode mynode = mystack.pop();
			if (mynode != null) {
				System.out.println(mynode.getKey() + "   " + mynode.getValue());
			} else {
				System.out.println("����ȱʧ");
			}
		}
	}
}
