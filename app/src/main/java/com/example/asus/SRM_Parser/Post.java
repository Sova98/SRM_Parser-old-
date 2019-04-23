package com.example.asus.SRM_Parser;

import java.util.concurrent.atomic.AtomicBoolean;

public class Post {
    private String photo_id;
    private String alb_id;
    private String description;
    private String name;
    private String price;
    private String normPhoto;
    private String photoThumb;

    public Post(String id, String text, String url_photo, String url_photoThumb, String alb_id) {
        this.photo_id = id;
        this.description = text;
        this.normPhoto = url_photo;
        this.alb_id = alb_id;
        this.photoThumb = url_photoThumb;
        makeNameAndPrice(description);
    }

    public Post() {
    }


    private void makeNameAndPrice(String description) {
        String lastWord = ""; StringBuffer lastName = new StringBuffer("");
        AtomicBoolean flag = new AtomicBoolean(false);
        this.name = "";
        this.price = "нету";
        //ищем цену 1250р 2450(цифр больше 2) больше одной цены см описание 2к
        int pos = 0;
        String[] words = description.split("[^0-9a-zA-Zа-яА-Я]+"); //все равно пробелы не убирает все
        for (String word : words) {
            if ( (word.equalsIgnoreCase("т") & word.length() == 1) || word.toLowerCase().contains("тыс") ){ //tolewercase чтобы искал без учета регистра
                if (lastWord.matches("[-+]?\\d+") ) //если предыдущее слов - число
                {
                    pos++;
                    this.price = word + "000";
                }
            }
            else if (!word.equals("")) {
                int i = 0;
                while (!(i + 1 >= word.length()) & (word.charAt(i) == '1' || word.charAt(i) == '2' || word.charAt(i) == '3' || word.charAt(i) == '4' || word.charAt(i) == '5'
                        || word.charAt(i) == '6' || word.charAt(i) == '7' || word.charAt(i) == '8' || word.charAt(i) == '9'
                        || word.charAt(i) == '0')) i++;
                if (word.charAt(i) == '1' || word.charAt(i) == '2' || word.charAt(i) == '3' || word.charAt(i) == '4' || word.charAt(i) == '5'
                        || word.charAt(i) == '6' || word.charAt(i) == '7' || word.charAt(i) == '8' || word.charAt(i) == '9'
                        || word.charAt(i) == '0') i++;
                if (i >= word.length() & word.length() > 2) { //если i вышло за пределы слова
                    pos++; //this.price = word.substring(0, i);
                    if (lastWord.matches("[-+]?\\d+") ) this.price = lastWord+word; //если предыдущее слов - из цифр
                    else this.price = word;
                } else if (i < word.length() & i > 0)
                    if (word.substring(i).equalsIgnoreCase("руб") || word.substring(i).equalsIgnoreCase("р") || word.substring(i).equalsIgnoreCase("p")) {
                        pos++;
                        //this.price = word.substring(0, i);
                        if (lastWord.matches("[-+]?\\d+") ) this.price = lastWord+word; //если предыдущее слов - из цифр
                        else this.price = word;
                    }
                    else if (word.substring(i).equalsIgnoreCase("тыс") || word.substring(i).equalsIgnoreCase("т")) {
                        pos++;
                        this.price = word.substring(0, i) + "000";
                    }
            }
            //теперь обрабатываем название
            String a = findRoot(word, lastName, flag);
            if (!a.equals("") & !flag.get() & !this.name.contains(a))
                if (this.name.equals("")) this.name = a;
                else this.name+=", " + a;

            //запомнили последнее слово
            lastWord = word;
        }
        if (pos != 1) this.price = "см описание"; //для сортировки сделать так чтобы была макс и мин цена?
        System.out.print(this.price);
        System.out.print(this.name);

    }

    private String findRoot(String word, StringBuffer lastName, AtomicBoolean flag ){
        //lastName.append("Check");
        //flag.set(true);
        if (!flag.get())
            switch (alb_id) {
                case "222401877": //necrons
                    if (word.toLowerCase().contains("warrior") || word.toLowerCase().contains("вариор") || word.toLowerCase().contains("вориор")
                            || word.toLowerCase().contains("вариар") || word.toLowerCase().contains("вориар") || word.toLowerCase().contains("воин"))
                        word = "Necron Warriors";
                    else if (word.toLowerCase().contains("immortal") || word.toLowerCase().contains("imortal") || word.toLowerCase().contains("имморт")
                            || word.toLowerCase().contains("иморт")) word = "Necron Immortals";
                    else if (word.toLowerCase().contains("deathmar") || word.toLowerCase().contains("dethmar") || word.toLowerCase().contains("десмар")
                            || word.toLowerCase().contains("дезмар")) word = "Necron Dethmarks";
                    else if (word.toLowerCase().contains("lord") || word.toLowerCase().contains("лорд")) word = "Necron Lord/Overlord";
                    else if (word.toLowerCase().contains("cryptek") || word.toLowerCase().contains("криптек")) word = "Necron Cryptek";
                    else if (word.toLowerCase().contains("wraiths") || word.toLowerCase().contains("врайт") ) word = "Necron Canoptek Wraiths";
                    else if (word.toLowerCase().contains("scarab") || word.toLowerCase().contains("скараб") ) word = "Necron Canoptek Scarabs";
                    else if (word.toLowerCase().contains("pretorian") || word.toLowerCase().contains("praetorian") || word.toLowerCase().contains("преториан") ) word = "Necron Triarch Praetorians";
                    else if (word.toLowerCase().contains("blade") || word.toLowerCase().contains("блэйд")
                            || word.toLowerCase().contains("блейд") || word.toLowerCase().contains("байк")) word = "Necrons Tomb Blades";
                    else if (word.toLowerCase().contains("destro") || word.toLowerCase().contains("дестро")) { word = ""; lastName.append("destroyer"); flag.set(true);}
                    else word = "";
                    break;
            }
        else switch (alb_id) {
            case "222401877": //necrons
                if (lastName.toString().equals("destroyer")) { if (word.toLowerCase().contains("lord") || word.toLowerCase().contains("лорд")){
                    lastName.replace(0, lastName.length(),"");
                    flag.set(false);
                    word = "Necron Destroyer Lord/Overlord";
                }
                else {
                    lastName.replace(0, lastName.length(),"");
                    flag.set(false);
                    word = "Necron Destroyers";
                } }
                break;
        }
        return word;
    }
    public String getPhoto() {
        return normPhoto;
    }

    public void setPhoto(String albumId) {
        this.normPhoto = albumId;
    }

    public String getPhotoThumb() {
        return photoThumb;
    }

    public void setPhotoThumb(String photoThumb) {
        this.photoThumb = photoThumb;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String id) {
        this.photo_id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String text) {
        this.description = text;
        makeNameAndPrice(description);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        //return Integer.getInteger(this.price);
        return price;
    }
}