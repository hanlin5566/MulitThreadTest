package com.hl.thread;

/**
 * Create by hanlin on 2019年1月25日
 * volatitle 只保证可见性，直接写入主存，不写入工作内存，所以所有线程保证对其修改的可见。
 **/
public class VolatileTest {
	private volatile int i = 0;
	public static void main(String[] args) {
		//模拟5个线程并发写入,有写锁.
		VolatileTest volatileTest = new VolatileTest();
//		volatileTest.CAS(true);
		volatileTest.orderTest();
	}


	private int[] data = {9, 9, 9, 9, 9, 9, 9, 9, 9, 9};
	private boolean isReady = false;

	private void initData() {
		data[0] = 0;
		data[1] = 1;
		data[2] = 2;
		data[3] = 3;
		data[4] = 4;
		data[5] = 5;
		data[6] = 6;
		data[7] = 7;
		data[8] = 8;
		data[9] = 9;
		isReady = true;
	}

	private int sumData() {
		if (!isReady) {
			return -1;
		}
		int sum = 0;
		sum += data[0];
		sum += data[1];
		sum += data[2];
		sum += data[3];
		sum += data[4];
		sum += data[5];
		sum += data[6];
		sum += data[7];
		sum += data[8];
		sum += data[9];
		return sum;
	}

	public void orderTest(){
		new Thread(() -> {
			try {
				while (true) {
					int sum = sumData();
					if(sum > -1){
						System.out.println(sum);
						return;
					}
				}
			} catch (Exception ignored) {}
		}).start();


		new Thread(() -> {
			try {
				initData();
			} catch (Exception ignored) {}
		}).start();
	}



	/**
	 * @param isCAS 是否采用乐观锁
	 */
	private void CAS(boolean isCAS) {
		int count = 5;
		for (int i = 0; i < count; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < 5000; j++) {
						//成功才退出
						//isCAS==true,CAS锁,线程安全.isCAS==false.线程非安全
						while(!isCASWrite(read(),read()+1,isCAS)){
							try {
								Thread.sleep(5);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					System.out.println(Thread.currentThread().getName()+" writed");
//					try {
//						cb.await();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					} catch (BrokenBarrierException e) {
//						e.printStackTrace();
//					}
				}
			});
			t.setName("Write Thread"+i);
			t.start();
		}

		//模拟5个线程并发读,无锁,所有的读应该都是立即可见的.
		for (int i = 0; i < count; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true){
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						int val = read();
						System.err.println(Thread.currentThread().getName()+" reaed :"+val);
					}
//					try {
//						cb.await();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					} catch (BrokenBarrierException e) {
//						e.printStackTrace();
//					}
				}
			});
			t.setName("Read Thread"+i);
			t.start();
		}
	}

	//直接写入,多线程时非线程安全.
	public boolean isCASWrite(int org,int target,boolean isCAS){
		if(isCAS)
			return write(org,target);
		else
			return write(target);
	}
	public boolean write(int target){
		this.i = target;
		return true;
	}
	//写入时采用自旋,采用乐观锁保证写入成功.
	public boolean write(int org,int target){
		if(this.i == org){
			this.i = target;
			return true;
		}
		return false;
	}

	public int read(){
		return i;
	}
}
