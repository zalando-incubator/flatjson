package bench;

public abstract class Benchmark {

//    public static class FlatJsonBenchmark extends Benchmark {
//        @Override protected String process(String input) {
//            return Json.parse(input)
//                    .asObject().get("TVUN8QNVHW")
//                    .asObject().get("D0jukTjAmn")
//                    .asArray().get(5)
//                    .toString();
//        }
//    }
//
//    public static class SimpleJsonBenchmark extends Benchmark {
//        @Override protected String process(String input) {
//            return com.eclipsesource.json.Json.parse(input)
//                    .asObject().get("TVUN8QNVHW")
//                    .asObject().get("D0jukTjAmn")
//                    .asArray().get(5)
//                    .toString();
//        }
//    }
//
//    public static class BoonBenchmark extends Benchmark {
//        private static final ObjectMapper mapper = JsonFactory.create();
//
//        @Override protected String process(String input) {
//            Map one = mapper.readValue(input, Map.class);
//            Map two = (Map) one.get("TVUN8QNVHW");
//            List three = (List) two.get("D0jukTjAmn");
//            return mapper.writeValueAsString(three.get(5));
//        }
//    }
//
//    public static class GsonBenchmark extends Benchmark {
//        private static final Gson gson = new Gson();
//
//        @Override protected String process(String input) {
//            Map one = gson.fromJson(input, Map.class);
//            Map two = (Map) one.get("TVUN8QNVHW");
//            List three = (List) two.get("D0jukTjAmn");
//            return gson.toJson(three.get(5));
//        }
//    }

}
