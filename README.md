# 设计说明
## 效果图
![1](https://github.com/Qcer17/JavaFinal/blob/master/img/1.png)
![2](https://github.com/Qcer17/JavaFinal/blob/master/img/2.png)
## 主要设计思路
- 根据作业要求，实现的场景为：在13×15的地图上，双方在两侧按阵型排列，阵型可选择，地图两侧显示血量，战斗开始后每回合双方朝敌人前进，在一定视野范围内且在子弹射击范围内时发现敌人后发射子弹，被击中掉血，血量为0后头像翻转静止，直到一方全部死亡。规定每回合各生物体移动一格或发射子弹或静止，同时只有在某回合子弹与敌方生物体恰好在某一格遭遇才进行伤害计算，在半格处遭遇不认为子弹击中敌人。
- 该次作业实际上可以看成是一种游戏，既然是游戏，那么每场游戏由系统固定其属性，如战场大小、角色数量、随机数种子等等，之后就可以生成一场游戏。于是很自然地可以抽象出两个对象Game和GameSystem，Game为每场游戏其本身，GameSystem则管理游戏、确定其各种属性、处理游戏中产生的事件、保存和读取游戏等。
- 在该游戏中有许多物体，包括葫芦娃、妖怪、子弹等，这些物体有一些共同的特征，如都有各自的图片、位置属性、可以移动等，因此为这些物体设置一个共同的基类CVMObject（CVM：Calabashes Vs Monsters）。葫芦娃和妖怪都是生物体，都有生命值、攻击力等属性，同时生物体都被要求实现为独立的线程，所以为其指定实现Runnable接口的虚基类Creature。
- 对于战斗中出现的各种事件，如位置移动、发射子弹、伤害计算等，设计类PositionManager和AttackManager处理这些事件，这两个类的对象作为GameSystem类的成员共同完成管理游戏的职责。
- 对于生物体线程之间的同步，使用CyclicBarrier对象作为同步机制，CyclicBarrier为多个线程设置屏障点，所有线程都到达后可以执行一段代码。因为该游戏有全局时钟，每回合每个生物体可以移动或发射子弹，等待所有生物体动作完成并且伤害计算后下回合方能开始，于是产生了生物体线程和伤害计算等线程之间的相互等待，同时到达屏障点后需要PositionManager计算下一回合各生物体的移动方向，所以使用CyclicBarrier作为同步手段，由类GloabalSynchronizer提供一个全局的CyclicBarrier对象。
- 由于将生物体实现为线程只是因为作业要求，所以没有必要将其他移动物体如子弹也实现为单独的线程，所以将子弹的处理都放在类AttackManager中。另外，为了避免重复生成子弹对象，使用类AttackBallContainer，该类提前生成一些子弹对象并设置为不可见，当场景需要有子弹时再由该类提供一个可用的子弹对象，当子弹超出地图即消失后由该类进行回收，从而实现重用。
- 阵型方面，规定了Formation接口，所有阵型都实现该接口，可由FormationProvider类获取各种阵型对象。
- 另外还有一些辅助类如属性类Attribute，位置类Position，枚举方向类Direction等。
- 因为每场游戏的结果在本实现中不依赖于线程调度（位置更新由PositionManager统一处理，生物体线程只需根据处理结果移动即可），所以保存和读取游戏的功能可以直接通过写入和读取游戏初始化参数来完成。
## UML类图
- 物体
![3](https://github.com/Qcer17/JavaFinal/blob/master/img/object.png)
- 阵型
![4](https://github.com/Qcer17/JavaFinal/blob/master/img/formation.png)
- 游戏系统
![5](https://github.com/Qcer17/JavaFinal/blob/master/img/gamesystem.png)
## 面向对象的体现
### 封装
封装对外隐藏内部的实现，于是保证了内部实现的变化不会影响外部使用，几乎所有public修饰的方法都具有这一特征。
- 阵型提供的接口不变，其内部具体如何计算不对外界产生任何影响。

```java
new SnakeFormation().format(...);
```
- 返回某物体下一回合的前进方向的PositionManager的getNextDirection方法，其内部可以是随机返回方向，也可以是经过计算后返回的合适的方向，对调用该方法的对象来说得到的都是合法的返回值（实际上返回的是使用BFS搜索得到的一条通向某一敌人的路径的第一步移动方向）。

```java
public Direction getNextDirection(int id, boolean isCalabash);
```
- 与上相似，获取某一位置附近的敌人的相对方位的方法nearbyEnemyDirection。

```java
public Direction nearbyEnemyDirection(Position p, boolean isCalabash);
```

### 继承
继承在该作业中得到了广泛应用，具体可见UML类图部分，这里不再赘述。

### 多态
多态具体体现在对同一个接口有多种不同的实现，从而在使用同一个接口时可以表现出不同的行为。
- 阵型接口Formation，有6个类实现了这一接口，于是只需要改变Formation对象的引用所指向的具体对象即可排列不同阵型。

```java
interface Formation(){
	public void format(Game game, List<? extends Creature> creatures, boolean left);
}

class SnakeFormation implements Formation(){...}
class SquareFormation implements Formation(){...}
class CraneFormation implements Formation(){...}
class GooseFormation implements Formation(){...}
class CrossFormation implements Formation(){...}
class ScaleFormation implements Formation(){...}
```

- Creature类是所有生物体的虚基类，没有实现Runnable接口，因此由继承自Creature的Calabash和Monster类进行实现，二者在攻击方式上有所不同，因此Runnable接口的实现有差异，从而尽管同为Creature线程但行为不同。

```java
public abstract class Creature extends CVMObject implements Runnable{...}
public class Calabash extends Creature{
	...
	@Override
	public void run(){...}
}
public class Monster extends Creature{
	...
	@Override
	public void run(){...}
}
```
## 主要体现的设计原则
### SRP 单一职责原则
如一些辅助类Attribute、Position等，引起他们变化的只有一个原因，即属性或位置的改变。再例如FormationProvider，只负责对外提供Formation对象，没有其他职责。
### LSP Liskov替换法则
派生类型应当可以替换其基类，这一点在物体相关的类的继承上可以得到体现。
### ISP 接口隔离原则
接口应精简单一，体现在Formation接口和Runnable接口，只需要重写一个方法。
### DIP 依赖倒置原则
要求面向抽象编程，具体而言所有的继承都应终止于接口或虚类，由UML类图可见，所有的继承终止于接口Formation、Runnable和虚类CVMObject以及Creature。
### CARP 合成/聚合复用原则
体现在GameSystem聚合了一些类的对象。

## 使用到的Java机制
### 异常处理
在读写文件和线程相关的代码处大量使用了异常处理机制。

```java
public static void save(File file) {
	try {
		FileWriter fileWriter = new FileWriter(file);
		...
	} catch (IOException e) {
		e.printStackTrace();
	}
}
```

```java
@Override
public void run() {
	try {
		barrier.await();
	} catch (InterruptedException | BrokenBarrierException e) {
		e.printStackTrace();
	}
}
```
### 集合类型与泛型
如Game类中存储葫芦娃和妖怪对象的链表以及format的形参：

```java
public class Game {
	private ArrayList<Calabash> calabashes = new ArrayList<>();
	private ArrayList<Monster> monsters = new ArrayList<>();
	...
}
```
```java
public interface Formation {
	public void format(Game game, List<? extends Creature> creatures, boolean left);
}
```
### 注解
重写基类方法、测试、作者和版本信息等。

```java
@Override
public void run() {...}
```
```java
@Override
public void format(...) {...}
```
```java
@Test
public void testLife() {...}
```

```java
/**
 * Attributes of creature in CVM.
 * 
 * @author Tian Qin
 * @version 1.0.0
 */
```
### 输入输出
读写文件。

```java
public static void save(File file) {
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(...);
			...
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
```
```java
public static void read(File file) {
	FileReader fileReader;
	try {
		fileReader = new FileReader(file);
		calabashFormation = fileReader.read();
		...
	} catch (IOException e) {
		e.printStackTrace();
	}
}
```

### 线程同步
使用CyclicBarrier对生物体线程、伤害计算线程和位置管理线程进行同步，具体而言每个对象都有GlobalSynchronizer对象创建的同一个CyclicBarrier对象的引用，到达屏障点后等待。另外，在伤害计算线程中为避免出现同一生物体死亡后仍受到攻击的情况，需要对计算伤害的方法进行加锁。在此说明一点，生物体位置同步在本实现中是不需要的，因为每回合的位置由PositionManager计算给出而不是生物体自行判断，不会出现冲突。


```java
public class GlobalSynchronizer {
	private CyclicBarrier barrier = new CyclicBarrier(calabashes.size() + monsters.size() + 1, new Runnable() {
		@Override
		public void run() {
			GameSystem.getPositionManager().updateBoard();
			GameSystem.getAttackManager().updateTurn();
		}
	});
	...
}
```
```java
public class AttackManager implements Runnable{
	public synchronized void processDamage(Creature attacker, Creature defender){...}
	...
}
```
## 单元测试
对属性更新、位置更新、寻路过程进行了单元测试。
## 使用说明
- 空格：开始，游戏结束后重置
- S：保存当前游戏，可在任何时刻进行
- L: 读取游戏，可在任何时刻进行，读取后按空格开始播放
- 两侧按钮：改变阵型
- 提供的一个游戏记录是src同级目录下的GameFile文件，使用L进行读取即可
