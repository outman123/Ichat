import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * Created by Administrator on 2017/12/11.
 */
class CellRenderer extends JLabel implements ListCellRenderer {
    CellRenderer() {
        setOpaque(true);//设置是否透明
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));// 加入宽度为5的空白边框

        if (value != null) {
            setText(value.toString());
            setIcon(new ImageIcon("sourse/头像.png"));
        }
        if (isSelected) {
            setBackground(new Color(255, 255, 153));// 设置背景色
            setForeground(Color.black);
        } else {
            // 设置选取与取消选取的前景与背景颜色.
            setBackground(Color.white); // 设置背景色
            setForeground(Color.black);
        }
        setEnabled(list.isEnabled());
        setFont(new Font("sdf", Font.ROMAN_BASELINE, 13));
        setOpaque(true);
        return this;
    }
}


public class ListModel extends AbstractListModel{

    private Vector vs;

    public ListModel(Vector vs){
        this.vs = vs;
    }

    @Override
    public Object getElementAt(int index) {
        // TODO Auto-generated method stub
        return vs.get(index);
    }

    @Override
    public int getSize() {
        // TODO Auto-generated method stub
        return vs.size();
    }

}

