package Stack;
import java.util.concurrent.atomic.*;//�ֹۣ�CAS�㷨��ԭ�ӱ���
public class CASStack {
	//ԭ�ӱ�������
	private AtomicReference<LinkNode> head = new AtomicReference<>();

	//new100 ,old  100
	//A  old100 new101 ,B old100 new 101
	//+1,+1
	//102

	public LinkNode pop() {
		LinkNode newhead;
		LinkNode oldhead;
		do {   //CAS����˼·�� ��������߳�get����ֵ��oldhead����������������ȡ����ֵ��newhead�� �������ϵ�ԭ��һ��Դ�Ըô洢������һ���̸߳��¹���������ֹ�޸ģ�����һ�ε����ٶ������ġ���һ�Ρ����ԡ���
//			��������߳�get����ֵ��oldhead����ȫ������������ȡ����ֵ��newhead�� �� ˵���ô洢���ڱ��̲߳����ڼ�δ����һ���̸߳��¹�����
//			����Է��ĵ���Ҫ�޸ĵ�Ŀ��ֵ�����ֵ��ͬʱ������ֵ��  newhead.setNext(oldhead);��
			oldhead = head.get();//ץȡ�����͵ȴ�  -��ԭ�ӵȴ��������ԡ�
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
		do {   //CAS����˼·�� ��������߳�get����ֵ��oldhead����������������ȡ����ֵ��newhead�� �������ϵ�ԭ��һ��Դ�Ըô洢������һ���̸߳��¹���������ֹ�޸ģ�����һ�ε����ٶ������ġ���һ�Ρ����ԡ���
//			��������߳�get����ֵ��oldhead����ȫ������������ȡ����ֵ��newhead�� �� ˵���ô洢���ڱ��̲߳����ڼ�δ����һ���̸߳��¹�����
//			����Է��ĵ���Ҫ�޸ĵ�Ŀ��ֵ�����ֵ��ͬʱ������ֵ��  newhead.setNext(oldhead);��
			oldhead = head.get();//�ȴ�   -��ԭ�ӵȴ��������ԡ�
			newhead.setNext(oldhead);//ѭ������
		} while (!head.compareAndSet(oldhead, newhead));
		//��������Դ���ȴ�������
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
