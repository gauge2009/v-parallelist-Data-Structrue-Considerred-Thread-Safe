package Stack;

public class LinkStackUnsafe {

	public LinkNode top = null;
	private int length = 0;

	public LinkStackUnsafe() {
		top = new LinkNode(0, "null");
	}


	public LinkNode pop() {
		synchronized (this) {
			top = top.getNext();
			length--;
			return top;
		}
	}

	public void push(LinkNode ln) {
		synchronized (this) { //悲观锁
			if (ln != null) {
				ln.setNext(top);
				top = ln;
				length++;
			}
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


	  // 单线程操作一个栈
	public static void main1(String[] args) {
		// TODO Auto-generated method stub
		LinkStackUnsafe mystack = new LinkStackUnsafe();
		for (int i = 0; i <= 20; i++) {
			mystack.push(new LinkNode(i, "A" + i));
		}
		for (int i = 0; i <= 20; i++) {
			LinkNode mynode = mystack.pop();
			System.out.println(mynode.getKey() + "   " + mynode.getValue());
		}
	}


	// 多线程操作一个栈
	public static void main(String[] args) throws InterruptedException {
		LinkStackUnsafe mystack = new LinkStackUnsafe();
		int N = 200;
		Thread[] ths = new Thread[N];
		for (int i = 0; i < N; i++) {
			int temp = i;
			Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
					mystack.push(new LinkNode(temp, "A" + temp));
				}
			});
			th.start();
			ths[temp] = th;
		}
		for (int i = 0; i < N; i++) {
			ths[i].join();
		}
		//操作缺失，
		for (int i = 0; i < N; i++) {
			LinkNode mynode = mystack.pop();
			if (mynode != null) {
				System.out.println(mynode.getKey() + "   " + mynode.getValue());
			} else {
				System.out.println("数据缺失");
			}
		}
	}
}
