package ge.unknown.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class AllDoctorsSingleton {

    private List<Doctor> doctors = new ArrayList<>();
    private Map<Long, String> map = new HashMap<>();

    private static AtomicReference<AllDoctorsSingleton> instance = new AtomicReference<>();

    private AllDoctorsSingleton() {
        map.put(986L, "თბილისი");
        map.put(988L, "გორი");
        map.put(992L, "რუსთავი");
        map.put(1010L, "თელავი");
        map.put(898L, "ქუთაისი");
        map.put(1006L, "ტყიბული");
        map.put(908L, "ბათუმი");
        map.put(890L, "თბილისი");
        map.put(914L, "სენაკი");
        map.put(990L, "რუსთავი");
        map.put(944L, "თბილისი");
        map.put(1012L, "კასპი");
        map.put(1018L, "თბილისი");
        map.put(920L, "თბილისი");
        map.put(910L, "რუსთავი");
        map.put(956L, "სამტრედია");
        map.put(978L, "მცხეთა");
        map.put(924L, "ხაშური");
        map.put(1002L, "გარდაბანი");
        map.put(892L, "თბილისი");
        map.put(938L, "ხაშური");
        map.put(980L, "მცხეთა");
        map.put(886L, "თბილისი");
        map.put(940L, "ფოთი");
        map.put(888L, "თბილისი");
        map.put(998L, "ქუთაისი");
        map.put(936L, "ხაშური");
        map.put(926L, "თბილისი");
        map.put(946L, "ზესტაფონი");
        map.put(996L, "ბათუმი");
        map.put(874L, "თბილისი");
    }

    public static AllDoctorsSingleton getInstance() {
        AllDoctorsSingleton foo = instance.get();
        if (foo == null) {
            foo = new AllDoctorsSingleton();
            if (instance.compareAndSet(null, foo))
                return foo;
            else
                return instance.get();
        } else {
            return foo;
        }
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public List<Doctor> findDoctors(String city) {
        List<Doctor> result = new ArrayList<>();
        if (city != null || !city.equals("")) {
            for (Map.Entry<Long, String> entry : map.entrySet())
            {
                if (entry.getValue().equals(city)) {
                    result.addAll(this.getDoctors().stream().filter(item-> item.getDoctorid().equals(entry.getKey())).collect(Collectors.toList()));
                }
            }
        }
        return result;
    }

    public void setDoctors(List<Doctor> doctors) {
        getInstance().doctors.clear();
        getInstance().doctors.addAll(doctors);
    }
}
