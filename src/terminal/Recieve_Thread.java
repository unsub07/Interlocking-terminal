//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

class Recieve_Thread implements Runnable {

    @Override
    public void run() {
        try {
            if (Net.clientSocket.isConnected() && Net.clientSocket != null && !Net.clientSocket.isClosed()) {

                String msgRecieved;

                while ((msgRecieved = Net.is.readLine()) != null) {
                    if (Exit.isEXIT()){
                        Log.log("Recieve_Thread return");
                        return;
                    }
                    if (msgRecieved.contains("BYE")||msgRecieved.contains("OK")) {// Сервер нас отключил
                        Log.log("//bye - Сервер нас отключил");
                        break;
                    }

                    String line = msgRecieved.trim();// .toUpperCase();//без
                    // пробелов малелькими
                    // буквами

                    String[] s = line.split(":");

                    String cmd = s[0].toUpperCase();

                    switch (cmd) {
                        case "ALARM":
                            // ALARM:1423063081613|8592|108|D
                            String[] a = s[1].split("\\|");
                            long dt = Long.valueOf(a[0]);
                            int id_obj = Integer.valueOf(a[1]);
                            int mes = Integer.valueOf(a[2]);
                            Alarms.alarm_kvit(dt, mes, id_obj);
                            break;

                        case "STATUS":
                            Area.status.setStatus(line);
                            break;
                        // case "NOTE":
                        // Note(line);
                        // break;
                        case "TIME":
                            if (s.length > 1) {
                                Clock.SetTime(s[1]);
                            }
                            break;
                        // case "CMDMESS":
                        // Event.InsertCmdMessage(line);
                        // break;
                        // case "EXIT":
                        // client_exit():
                        // break;
                        case "IND":
                            // System.out.println(line);
                            // new SetState(line).start();
                            SetState.s0(line);
                            break;
                        case "VGN_LST":

                            Commander.vgn.dispose();

                            Log.log(" **********VGN LST*********** " + line);
                            String[] a1 = s[1].split("\\|");
                            if (!"NO DATA".equals(a1[0])){
//                                long dt1 = Long.valueOf(a1[0]);
//                                int id_obj1 = Integer.valueOf(a1[1]);
                                String k = String.valueOf(a1[2]);
                                if (!"null" .equals(k)){
                                    Vgn.Show(s[1]);
                                }
                                // КодКартинки
                                // СостояниеКод
                                // ТехнологическаяОперацияКод
                                // РодГрузаКод
                                //? ЖД Оператор
                                //? Контрагент
                            }
                            break;
                        default:
                            // new SetState(line).start();//YES
                            Log.log("Receive_Thread default. " + line);
                            break;
                    }
//                    Log.log("end switch");
                }// end while
                Log.log("end while");
            }// end if
            Log.log("end if");
        } catch (java.io.IOException e) {
// тут возникает Read timed out или Socket closed
            Log.log("1 ioEx Net.Disconnect catch Receive Thread");
            Net.Disconnect();// Exception in thread "Thread-2"
            // java.lang.NullPointerException
        }
        // тут по return вышли
        // end catch// end catch
        Log.log("2 end while message=null - timeout Net.Disconnect Receive Thread end");
        Net.Disconnect();//делаем дисконнект для статуса!!!
    }// end run
}// end class recievethread
