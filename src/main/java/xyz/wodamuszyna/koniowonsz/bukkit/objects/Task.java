package xyz.wodamuszyna.koniowonsz.bukkit.objects;

import java.util.ArrayList;
import java.util.List;

public class Task {
    public List<TaskData> tasks = new ArrayList<>();

    public void addTask(TaskData task){ tasks.add(task); }

    public class TaskData{
        public String username;
        public String command;

        public TaskData(String username, String command){
            this.username = username;
            this.command = command;
        }
    }
}
