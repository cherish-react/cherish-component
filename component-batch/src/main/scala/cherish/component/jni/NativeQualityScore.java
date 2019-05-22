package cherish.component.jni;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/4/17
 */
public class NativeQualityScore {
    /**
     * 获得分数和质量图
     * @param img 压缩图
     *
     * jfloat Java_com_xx_getQualityScore(JNIEnv *env
     *                   , jclass obj
     *                   , jbyteArray srcImg
     *                   , jint width, jint height, jint fgp, jint Core_X, jint Core_Y, jint localSize, jint stepSize, jint modeNumber);
     */
    public static native QualityImage GetQualityScore(byte[] img,byte[] mnt,int fgp,String className);

//    public static void main(String[] args){
//        new JniLoader().loadJniLibrary("./support","");
//        System.out.println("");
//    }
}
