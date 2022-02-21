//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

interface Globals {

//    int TunePngX = -6;
//    int TunePngY = -6;
//    int DEF_WIDTH = 84;
//    int DEF_HEIGHT = 84;

    // Railnet layers
    int RAILNET_EMPTY_LR = 1;
    int RAILNET_BLOCK_LR = 2;
    int RAILNET_ROUTE_LR = 3;
    int RAILNET_M_ROUTE_LR = 4;
    int RAILNET_UKNOWN = 5;
    int RAILNET_BUSY_LR = 6;
    int RAILNET_BUSY_UNKNOWN_LR = 7;
    int RAILNET_IR_LR = 8;
    int RAILNET_IR_UNKNOWN_LR = 9;
    int RAILNET_SKID_LR = 11;
       
    //menu
    String CAUNTION = "Внимание";
    String OPERATION = "Ответственная операция";

    String r_ir_menu_item = "<html>запуск <b><font color=orange>искусственной разделки</font></b></html>";
    String r_block_menu_item = "<html>блокировка снята, <b><font color=orange>установить</font></b></html>";
    String r_unblock_menu_item = "<html>блокировка установлена, <b><font color=green>снять</font></b></html>";
    String r_skid_menu_item = "<html>башмаки не установлены, <b><font color=orange>установить</font></b></html>";
    String r_unskid_menu_item = "<html>башмаки установлены, <b><font color=green>снять</font></b></html>";
    String r_hollowCancel_menu_item = "<html>сброс <b><font color=green>ложной занятости</font></b></html>";
    String r_bomm_menu_item = "<html><b><font color=green>Согласие на ограждение пути</font></b></html>";
    String r_boomcancel_menu_item = "<html>Аварийная <b><font color=green>отмена ограждения</font></b></html>";

    String c_open_menu_item = "переезд закрыт, ОТКРЫТЬ";
    String c_close_menu_item = "переезд открыт, ЗАКРЫТЬ";
    String c_enable_notif_menu_item = "разрешить подачу извещения";
    String c_disable_notif_menu_item = "запретить подачу извещения";

    String f_menu_item_light = "<html>Облегченная обдувка., <b><font color=orange>Включить</font></b></html>";
    String f_menu_item_norm = "<html>Нормальная обдувка., <b><font color=orange>Включить</font></b></html>";
    String f_menu_item_hard = "<html>Усиленная обдувка., <b><font color=orange>Включить</font></b></html>";
    String f_menu_item_off = "<html>Отключение обдувки., <b><font color=green>Выключить</font></b></html>";

    String l_block_off_menu_item = "<html>блокировка снята, <b><font color=orange>установить</font></b></html>";
    String l_block_on_menu_item = "<html>блокировка установлена, <b><font color=green>снять</font></b></html>";
    String l_on_menu_item = "<html><b><font color=blue>включение</font></b> пригласительного</html>";
    String l_off_menu_item = "<html><b><font color=red>выключение</font></b> пригласительного</html>";
    String l_repeat_menu_item = "<html><b><font color=orange>повтор</font></b> пригласительного</html>";
    String l_recover_menu_item = "<html><b><font color=green>восстановление</font></b> (повторное открытие)</html>";

    String p_notif_on = "<html>Оповещение выкл., <b><font color=orange>Включить</font></b></html>";
    String p_notif_off = "<html>Оповещение вкл., <b><font color=green>Выключить</font></b></html>";
    String p_all_notifi_on = "<html>Все зоны. Оповещение выкл., <b><font color=orange>Включить</font></b></html>";
    String p_all_notif_off = "<html>Все зоны. Оповещение вкл., <b><font color=green>Выключить</font></b></html>";

    String t_plus_menu_item = "<html>перевод в <b><font color=green>ПЛЮС</font></b></html>";
    String t_minus_menu_item = "<html>перевод в <b><font color=orange>МИНУС</font></b></html>";
    String t_auxplus_menu_item = "<html>вспомогательный перевод в <b><font color=green>ПЛЮС</font></b></html>";
    String t_auxminus_menu_item = "<html>вспомогательный перевод в <b><font color=orange>МИНУС</font></b></html>";
    String t_block_menu_item = "<html>блокировка снята, <b><font color=orange>установить</font></b></html>";
    String t_unblock_menu_item = "<html>блокировка установлена, <b><font color=green>снять</font></b></html>";
    String t_fann_menu_item = "<html>обдувка, <b><font color=orange>включить</font></b></html>";

}
