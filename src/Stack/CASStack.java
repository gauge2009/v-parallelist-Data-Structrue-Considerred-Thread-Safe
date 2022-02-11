package Stack;
import java.util.concurrent.atomic.*;//乐观，CAS算法的原子变量
public class CASStack {
	//原子变量引用
	private AtomicReference<LinkNode> head = new AtomicReference<>();

	//new100 ,old  100
	//A  old100 new101 ,B old100 new 101
	//+1,+1
	//102

	public LinkNode pop() {
		LinkNode newhead;
		LinkNode oldhead;
		do {   //CAS核心思路： 如果工作线程get到的值【oldhead】不符合期望它读取到的值【newhead】 【不符合的原因一般源自该存储区被另一个线程更新过】，就终止修改，再来一次迭代再读（“赌”）一次【重试】，
//			如果工作线程get到的值【oldhead】完全符合期望它读取到的值【newhead】 【 说明该存储区在本线程操作期间未被另一个线程更新过】，
//			则可以放心地用要修改的目标值替代旧值，同时步进新值（  newhead.setNext(oldhead);）
			oldhead = head.get();//抓取不到就等待  -【原子等待】【重试】
			if (oldhead == null) {
				return null;
			}
			newhead = oldhead.getNext();


		} while (!head.compareAndSet(oldhead, newhead));

		return oldhead;
	}

	public void push(LinkNode ln) {
		LinkNode newhead = new LinkNode(ln.getKey(), ln.getValue());
		LinkNode oldhead;
		do {   //CAS核心思路： 如果工作线程get到的值【oldhead】不符合期望它读取到的值【newhead】 【不符合的原因一般源自该存储区被另一个线程更新过】，就终止修改，再来一次迭代再读（“赌”）一次【重试】，
//			如果工作线程get到的值【oldhead】完全符合期望它读取到的值【newhead】 【 说明该存储区在本线程操作期间未被另一个线程更新过】，
//			则可以放心地用要修改的目标值替代旧值，同时步进新值（  newhead.setNext(oldhead);）
			oldhead = head.get();//等待   -【原子等待】【重试】
			newhead.setNext(oldhead);//循环插入
		} while (!head.compareAndSet(oldhead, newhead));
		//抢不到资源，等待，重试
	}

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		CASStack mystack = new CASStack();
		int N = 100;
		Thread[] ths = new Thread[N];
		for (int i = 0; i < N; i++) {
			int temp = i;
			Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
					mystack.push(new LinkNode(temp, "A" + temp));
				}
			});
//			Thread th = new Thread(()->{
//				mystack.push(new LinkNode(temp, "A" + temp));
//			});
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
