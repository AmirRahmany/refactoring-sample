package com.hamkelasi.bll;
import com.hamkelasi.dal.Base;

public class City {
    private int id;
    private String name;
    private int provinceId;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public City() {
    }

    public City(int id) {
        final com.hamkelasi.dal.City city = new com.hamkelasi.dal.City();
        // Assuming DAL returns a City object; adjust if DAL uses a different structure
        final Base.Row dr = city.get(id);

        this.id = Integer.parseInt(dr.get("id").toString());
        this.name = dr.get("cityName").toString();
        this.provinceId = Integer.parseInt(dr.get("provinceId").toString());
    }

    public static int getCount() {
        final com.hamkelasi.dal.City city = new com.hamkelasi.dal.City();
        return city.getCount();
    }

    public static int getCount(int provinceId) {
        final com.hamkelasi.dal.City city = new com.hamkelasi.dal.City();
        return city.getCountByProvince(provinceId);
    }

    public static City[] getList(int provinceId) {
        final com.hamkelasi.dal.City city = new com.hamkelasi.dal.City();
        // Assuming DAL returns List<City> for simplicity
        java.util.List<Base.Row> dalCityList = city.getList(provinceId);
        City[] cityList = new City[dalCityList.size()];

        for (int i = 0; i < dalCityList.size(); i++) {
            cityList[i] = new City();
            cityList[i].id = Integer.parseInt(dalCityList.get(i).get("id").toString());
            cityList[i].name = dalCityList.get(i).get("cityName").toString();
            // ProvinceID not set here in C#, so omitted; include if needed
        }

        return cityList;
    }

    public int update(int id, String cityName, int provinceId) {
        final com.hamkelasi.dal.City city = new com.hamkelasi.dal.City();
        int count = city.getCount(cityName, provinceId);

        if (count == 0) {
            boolean isUpdated = city.update(id, cityName, provinceId);

            if (isUpdated) {
                return 0; // اطلاعات با موفقیت تغییر یافت
            } else {
                return 9; // اشکال در تغییر اطلاعات
            }
        } else {
            return 1; // شهری با این نام در استان مربوط وجود دارد
        }
    }

    public int add(String name, int provinceId) {
        final com.hamkelasi.dal.City city = new com.hamkelasi.dal.City();
        int count = city.getCount(name, provinceId);

        if (count == 0) {
            boolean isAdded = city.add(name, provinceId);

            if (isAdded) {
                return 0; // ثبت داده با موفقیت انجام گرفت
            } else {
                return 9; // اشکال در ثبت داده
            }
        } else {
            return 1; // شهری با این نام در استان مربوط وجود دارد
        }
    }

    public int delete(int cityId) {
        final com.hamkelasi.dal.City city = new com.hamkelasi.dal.City();
        boolean isDeleted = city.delete(cityId);

        if (isDeleted) {
            return 0;
        } else {
            return 9; // اشکال در حذف شهر
        }
    }
}