package top.cubik65536.yuq.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class YuQStarterAdapter extends ClassVisitor {
	public YuQStarterAdapter(ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
		if (name.equals("start")) {
			mv = new YuQStarterMethodAdapter(mv);
		}
		return mv;
	}

	@SuppressWarnings("InnerClassMayBeStatic")
	class YuQStarterMethodAdapter extends MethodVisitor{

		private boolean status = false;

		public YuQStarterMethodAdapter(MethodVisitor mv){
			super(Opcodes.ASM5, mv);
		}

		@Override
		public void visitLdcInsn(Object value) {
			String str = value.toString();
			if (str.startsWith("感谢您使用 YuQ 进行开发")){
				try {
					String my = "    ___      ___      _   __      _   __      ___      ___    __  ___        / __      ___    __  ___ \n" +
									"  //   ) ) //   ) ) // ) )  ) ) // ) )  ) ) ((   ) ) //   ) )  / /    ____  //   ) ) //   ) )  / /    \n" +
									" //       //   / / // / /  / / // / /  / /   \\ \\    //___/ /  / /          //   / / //   / /  / /     \n" +
									"((____   ((___/ / // / /  / / // / /  / / //   ) ) //        / /          ((___/ / ((___/ /  / /      \n";
					value = "感谢您使用 YuQ 进行开发，在您使用中如果遇到任何问题，可以到 Github（ https://github.com/IceCream-Open/Rain 、 https://github.com/YuQWorks/YuQ ）提出 issue，您也可以添加 YuQ 的开发交流群\n" +
							"（Njk2MTI5MTI4）（Base64解码网站：http://tools.bugscaner.com/base64/ ）进行交流。\n" +
							my + "\n" +
							"欢迎使用LittleSkin社区支持机器人！";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			super.visitLdcInsn(value);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
			super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
			if (name.equals("<init>") && owner.equals("com/IceCreamQAQ/Yu/loader/AppClassloader")){
				status = true;
			}
		}

		@Override
		public void visitVarInsn(int opcode, int var) {
			super.visitVarInsn(opcode, var);
			if (status){
				super.visitLdcInsn("com.IceCreamQAQ.Yu.loader.AppLoader");
				super.visitMethodInsn(Opcodes.INVOKESTATIC, "top/cubik65536/yuq/asm/AddControllerAdapter", "asm",
						"(Ljava/lang/String;)[B", false);
				super.visitVarInsn(Opcodes.ASTORE, 4);
				super.visitVarInsn(Opcodes.ALOAD, 3);
				super.visitLdcInsn("com.IceCreamQAQ.Yu.loader.AppLoader");
				super.visitVarInsn(Opcodes.ALOAD, 4);
				super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/IceCreamQAQ/Yu/loader/AppClassloader", "define",
						"(Ljava/lang/String;[B)Ljava/lang/Class;", false);
				super.visitInsn(Opcodes.POP);

				super.visitMethodInsn(Opcodes.INVOKESTATIC, "top/cubik65536/yuq/asm/GenerateController", "generate",
						"()[B", false);
				super.visitVarInsn(Opcodes.ASTORE, 5);
				super.visitVarInsn(Opcodes.ALOAD, 3);
				super.visitLdcInsn("top.cubik65536.yuq.controller.ASMController");
				super.visitVarInsn(Opcodes.ALOAD, 5);
				super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/IceCreamQAQ/Yu/loader/AppClassloader", "define",
						"(Ljava/lang/String;[B)Ljava/lang/Class;", false);
				super.visitInsn(Opcodes.POP);
				status = false;
			}
		}
	}
}