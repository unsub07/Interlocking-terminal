//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

import ru.attrans.proc.objs.F3_DIO_20_8_02;

class f3_dio_20_8_02 {
    
    private long dtime;// последнее время  
    private final int id_obj;

    // --Commented out by Inspection (16.01.18 14:58):private boolean ModuleErrorCode11; // Отсутствует обработка ввода/вывода во время теста при загрузке - 130111
    // --Commented out by Inspection (16.01.18 14:58):private boolean ModuleErrorCode12; // Интерфейс изготовителя в режиме работы - 130121
    // --Commented out by Inspection (16.01.18 14:58):private boolean ModuleErrorCode14; // Отсутствует обработка ввода/вывода: неправильное параметрирование -130141
    // --Commented out by Inspection (16.01.18 14:58):private boolean ModuleErrorCode15; // Отсутствует обработка ввода/вывода: превышено допустимое количество ошибок -130151
    // --Commented out by Inspection (16.01.18 14:58):private boolean ModuleErrorCode16; // Отсутствует обработка входа/выхода: не вставлен конфигурированный модуль - 130161
    // --Commented out by Inspection (16.01.18 14:58):private boolean ModuleErrorCode17; // Отсутствует обработка входа/выхода: не вставлен конфигурированный модуль - 130171

    // --Commented out by Inspection (16.01.18 14:58):private boolean DIErrorCode30;//2 байт 0 бит - Ошибка в зоне цифровых входов  (IN) - 130301
    // --Commented out by Inspection (16.01.18 14:58):private boolean DIErrorCode31;//2 байт 1 бит - Тест FTZ образца тестирования содержит ошибку - 130311

    // --Commented out by Inspection (16.01.18 14:58):private boolean DOErrorCode50; //4 байт 0 бит - Ошибка в зоне цифровых выходов (OUT) -130501
    // --Commented out by Inspection (16.01.18 14:58):private boolean DOErrorCode51; //4 байт 1 бит - Тест MEZ предохранительного отключения показывает ошибку - 130511
    // --Commented out by Inspection (16.01.18 14:58):private boolean DOErrorCode52; //4 байт 2 бит - Тест MEZ вспомогательного напряжения показывает ошибку - 130521
    // --Commented out by Inspection (16.01.18 14:58):private boolean DOErrorCode53; //4 байт 3 бит - Тест FTZ образца тестирования показывает ошибку - 130531
    // --Commented out by Inspection (16.01.18 14:58):private boolean DOErrorCode54; //4 байт 4 бит - Тест MEZ образца тестирования выходного выключателя показывает ошибку - 130541
    // --Commented out by Inspection (16.01.18 14:58):private boolean DOErrorCode55; //4 байт 5 бит - Тест MEZ образца тестирования выходного выключателя (тест отключения выходов) показывает ошибку - 130551
    // --Commented out by Inspection (16.01.18 14:58):private boolean DOErrorCode56; //4 байт 6 бит - Тест MEZ активного отключения посредством WD показывает ошибку - 130561
    // --Commented out by Inspection (16.01.18 14:58):private boolean DOErrorCode41; //5 байт 1 бит - Все выходы отключены. превышен общий ток  - 130411
    // --Commented out by Inspection (16.01.18 14:58):private boolean DOErrorCode42; //5 байт 2 бит - Тест FTZ: температурный порог 1 превышен - 130421
    // --Commented out by Inspection (16.01.18 14:58):private boolean DOErrorCode43; //5 байт 3 бит - Тест FTZ: температурный порог 2 превышен - 130431
    // --Commented out by Inspection (16.01.18 14:58):private boolean DOErrorCode44; //5 байт 4 бит - Тест FTZ: контроль вспомогательного напряжения 1: пониженное напряжение - 130441

    // --Commented out by Inspection (16.01.18 14:58):private boolean DI_01_ErrorCode60; //6 байт 0 бит - Ошибка в модуле цифрового ввода
    // --Commented out by Inspection (16.01.18 14:58):private boolean DI_01_ErrorCode61; //6 байт 1 бит - Замыкание линии канала
    
    // --Commented out by Inspection (16.01.18 14:58):private boolean DO_01_ErrorCode260;//26 байт 0 бит - Ошибка в модуле цифрового ввода
    // --Commented out by Inspection (16.01.18 14:58):private boolean DO_01_ErrorCode261;//26 байт 1 бит - Выход отключен из-за перегрузки
    // --Commented out by Inspection (16.01.18 14:58):private boolean DO_01_ErrorCode262;//26 байт 2 бит - Ошибка при обратном считывании настройки цифровых выходов ?
    // --Commented out by Inspection (16.01.18 14:58):private boolean DO_01_ErrorCode264;//26 байт 4 бит - Ошибка при обратном считывании состояния цифровых выходов ?
    
    f3_dio_20_8_02(
                int ID_OBJ //0=ID_OBJ
//                int ID_AREA, //1=ID_AREA
//                String S_NAME //2=S_NAME
//                String MPC_NAME //3=MPC_NAME
    ) {
       super();// 30=F3 DIO
       id_obj = ID_OBJ;
    }
    
    void setState(long DTIME, byte[] b, int OBJID) {
        setDtime(DTIME);
//------0
//        boolean moduleErrorCode10;
        boolean err = (b[1] & 0b00000001) != 0x00;
        if ((b[1] & 0b00000001) != 0x00) {
            event();
//            alarm_on(13001);
        } //else {
            //            alarm_off(13001);
        //}
        Unit u = Terminal.Unit_Hash.get(OBJID);
        if (u != null){
            u.setFault(err);
        }
    }
    
    private void setDtime(long DTIME) {
        dtime = DTIME;
    }
    
    private long getDtime() {
        return dtime;
    }
        
    private void event() {
        if (getDtime() > 0) {
            Events.InsertMessage(getDtime(), id_obj, 130101);
        }
    }

// --Commented out by Inspection START (16.01.18 14:58):
//    private void alarm_on(int id) {
//        if (getDtime() > 0) {
//            Alarms.alarm_on(getDtime(), id, id_obj);
//        }
//    }
// --Commented out by Inspection STOP (16.01.18 14:58)

// --Commented out by Inspection START (16.01.18 14:58):
//    private void alarm_off(int id) {
//        if (getDtime() > 0) {
//            Alarms.alarm_off(getDtime(), id, id_obj);
//        }
//    }
// --Commented out by Inspection STOP (16.01.18 14:58)

    //@Override
    public void setState(Object oid) {
        F3_DIO_20_8_02 d = (F3_DIO_20_8_02) oid;
        
        setState(
                d.timestamp,
                d.rawData,
                d.objId// bytearray
        );
    }
}
