package com.skobelev.sercvice;


import com.skobelev.model.User;
import com.skobelev.model.UserRepository;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.api.APIException;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private Integer id = 0;


    public void createUser(Long chatId) {
        if (containsUser(chatId)) return;
        User user = new User(id++, chatId, "", 0);
        userRepository.save(user);
    }

    private boolean containsUser(Long chatId) {
        for (User user : userRepository.findAll()) {
            if (user.getChatId().equals(chatId)) return true;
        }
        return false;
    }

    public String printHelp() {

        StringBuilder sb = new StringBuilder();

        sb.append(">>> List of command <<<");
        sb.append(System.lineSeparator());
        sb.append("/status - can help you to see your profile;");
        sb.append(System.lineSeparator());
        sb.append("/start - call greeting;");
        sb.append(System.lineSeparator());
        sb.append("/setCity - set your city. Example Moscow, Saint Petersburg, Biysk, London, New York;");
        sb.append(System.lineSeparator());
        sb.append("/now - show wheare now in the city, which you set;");
        sb.append(System.lineSeparator());
        sb.append("/day - show wheare for 24 h;");
        sb.append(System.lineSeparator());
        sb.append("/later - show wheare in h, which you set;");
        sb.append(System.lineSeparator());

        return sb.toString();
    }

    public void userSetCity(Long chatId, String city) {

        for (User user : userRepository.findAll()) {

            if (user.getChatId().equals(chatId)) user.setUserCity(city);

            userRepository.save(user);

        }

    }

    public String printGreeting() {

        StringBuilder sb = new StringBuilder();

        sb.append("Hey ");
        sb.append(System.lineSeparator());
        sb.append("Nice to meet you. It is bot which can help you know wheater.");
        sb.append(System.lineSeparator());
        sb.append("If you need help, just write command /help.");

        return sb.toString();
    }

    public String printUserStatus(Long chatId) {

        StringBuilder sb = new StringBuilder();

        sb.append("Our status profile");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());

        for (User user : userRepository.findAll()) {

            if (user.getChatId().equals(chatId)) {

                sb.append("id >> " + user.getId());
                sb.append(System.lineSeparator());
                sb.append("chatId >> " + user.getChatId());
                sb.append(System.lineSeparator());
                sb.append("city >> " + user.getUserCity());
                sb.append(System.lineSeparator());
                sb.append("count >> " + user.getCount());
                sb.append(System.lineSeparator());
                sb.append(System.lineSeparator());

            }

        }

        return sb.toString();
    }

    public String printUserStatusAll() {

        StringBuilder sb = new StringBuilder();

        String count = Long.toString(userRepository.count());

        sb.append("Status of all user");
        sb.append(System.lineSeparator());

        sb.append("count >> " + count);
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());

        for (User user : userRepository.findAll()) {

            sb.append("id >> " + user.getId());
            sb.append(System.lineSeparator());
            sb.append("chatId >> " + user.getChatId());
            sb.append(System.lineSeparator());
            sb.append("city >> " + user.getUserCity());
            sb.append(System.lineSeparator());
            sb.append("count >> " + user.getCount());
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());

        }

        return sb.toString();
    }


    private final String API_KEY = "API_KEY";

    public String getWeather(Long chatId) throws APIException {

        StringBuilder sb = new StringBuilder();
        String city = "";

        OWM owm = new OWM(API_KEY);
        owm.setUnit(OWM.Unit.METRIC);

        for (User user : userRepository.findAll()) {

            if (user.getChatId().equals(chatId)) {
                city = user.getUserCity();
                int count = user.getCount();
                count++;
                user.setCount(count);
                userRepository.save(user);
            }

        }


        CurrentWeather currentWeather = owm.currentWeatherByCityName(city);

        sb.append("City >> " + currentWeather.getCityName());
        sb.append(System.lineSeparator());
        sb.append("Temperature >> " + currentWeather.getMainData().getTempMin() + " / " + currentWeather.getMainData().getTempMax() + " C");
        sb.append(System.lineSeparator());
        sb.append("Description >> " + currentWeather.getWeatherList().get(0).getDescription());
        sb.append(System.lineSeparator());
        sb.append("Date time >> " + currentWeather.getDateTime());
        sb.append(System.lineSeparator());

        return sb.toString();
    }


    public String getWeatherDay(Long chatId) throws APIException {

        StringBuilder sb = new StringBuilder();
        String city = "";

        OWM owm = new OWM(API_KEY);
        owm.setUnit(OWM.Unit.METRIC);

        for (User user : userRepository.findAll()) {

            if (user.getChatId().equals(chatId)) {
                city = user.getUserCity();
                int count = user.getCount();
                count++;
                user.setCount(count);
                userRepository.save(user);
            }

        }


        HourlyWeatherForecast hourlyWeatherForecast = owm.hourlyWeatherForecastByCityName(city);

        for (int i = 0; i < 9; i++) {
            sb.append("City >> " + hourlyWeatherForecast.getCityData().getName());
            sb.append(System.lineSeparator());
            sb.append("Temperature >> " + hourlyWeatherForecast.getDataList().get(i).getMainData().getTempMin() +
                    " / " + hourlyWeatherForecast.getDataList().get(i).getMainData().getTempMax() + " C");
            sb.append(System.lineSeparator());
            sb.append("Description >> " + hourlyWeatherForecast.getDataList().get(i).getWeatherList().get(0).getDescription());
            sb.append(System.lineSeparator());
            sb.append("Date time >> " + hourlyWeatherForecast.getDataList().get(i).getDateTime());
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }


    public String getWeatherDayUser(Long chatId, int hours) throws APIException {

        StringBuilder sb = new StringBuilder();
        String city = "";

        OWM owm = new OWM(API_KEY);
        owm.setUnit(OWM.Unit.METRIC);

        for (User user : userRepository.findAll()) {

            if (user.getChatId().equals(chatId)) {
                city = user.getUserCity();
                int count = user.getCount();
                count++;
                user.setCount(count);
                userRepository.save(user);
            }

        }


        HourlyWeatherForecast hourlyWeatherForecast = owm.hourlyWeatherForecastByCityName(city);

        int i = (int) Math.ceil((float)hours/3);

        sb.append("City >> " + hourlyWeatherForecast.getCityData().getName());
        sb.append(System.lineSeparator());
        sb.append("Temperature >> " + hourlyWeatherForecast.getDataList().get(i).getMainData().getTempMin() +
                " / " + hourlyWeatherForecast.getDataList().get(i).getMainData().getTempMax() + " C");
        sb.append(System.lineSeparator());
        sb.append("Description >> " + hourlyWeatherForecast.getDataList().get(i).getWeatherList().get(0).getDescription());
        sb.append(System.lineSeparator());
        sb.append("Date time >> " + hourlyWeatherForecast.getDataList().get(i).getDateTime());
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());


        return sb.toString();
    }

}
