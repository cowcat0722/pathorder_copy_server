package shop.project.pathorderserver.store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.project.pathorderserver._core.errors.exception.Exception400;
import shop.project.pathorderserver._core.errors.exception.Exception401;
import shop.project.pathorderserver._core.errors.exception.Exception404;
import shop.project.pathorderserver.menu.Menu;
import shop.project.pathorderserver.menu.MenuOptionRepository;
import shop.project.pathorderserver.menu.MenuRepository;
import shop.project.pathorderserver.menu.MenuOption;
import shop.project.pathorderserver.order.Order;
import shop.project.pathorderserver.order.OrderRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final OrderRepository orderRepository;

    // 매장 등록
    public Store 매장등록(StoreRequest.매장등록 reqDTO) {
        Store store = new Store(reqDTO);
        storeRepository.save(store);

        return store;
    }

    // 점주 로그인
    public Store 로그인(StoreRequest.로그인 reqDTO) {
        Store store = storeRepository.findByUsernameAndPassword(reqDTO.getUsername(), reqDTO.getPassword())
                .orElseThrow(() -> new Exception401("유저네임 또는 패스워드가 일치하지 않습니다."));

        return store;
    }

    // 매장 목록보기
    public List<StoreResponse.ListingsDTO> getStoreList() {
        List<Store> stores = storeRepository.findAll();

        return stores.stream().map(StoreResponse.ListingsDTO::new).toList();
    }

    // 매장 상세보기
    public StoreResponse.DetailDTO getStoreDetail(int storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new Exception404("찾을 수 없는 매장입니다."));

        return new StoreResponse.DetailDTO(store);
    }

    // 매장 상세보기 - 사업자 정보
    public StoreResponse.BizInfoDTO getStoreBizDetail(int storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new Exception404("찾을 수 없는 매장입니다."));

        return new StoreResponse.BizInfoDTO(store);
    }

    // 매장 메뉴보기
    public StoreResponse.MenuListDTO getStoreMenuList(int storeId) {
        Store store // 매장 정보
                = storeRepository.findById(storeId)
                .orElseThrow(() -> new Exception404("찾을 수 없는 매장입니다."));
        List<Menu> menus // 매장 메뉴 정보
                = menuRepository.findAllByStoreId(storeId)
                .orElseThrow(() -> new Exception404("찾을 수 없는 메뉴입니다."));

        return new StoreResponse.MenuListDTO(store, menus);
    }

    // 매장 메뉴 옵션보기
    public StoreResponse.MenuOptionDTO getStoreMenuDetail(int storeId, int menuId) {
        Store store // 매장 정보
                = storeRepository.findById(storeId)
                .orElseThrow(() -> new Exception404("찾을 수 없는 매장입니다."));
        Menu menu // 매장 메뉴 정보
                = menuRepository.findById(menuId)
                .orElseThrow(() -> new Exception404("찾을 수 없는 메뉴입니다."));
        List<MenuOption> optionList // 매장 메뉴 옵션 정보
                = menuOptionRepository.findByMenuId(menuId)
                .orElseThrow(() -> new Exception404("찾을 수 없는 옵션입니다."));

        return new StoreResponse.MenuOptionDTO(store, menu, optionList);
    }

    // 주문내역 목록보기 - 점주
    public StoreResponse.OrderListDTO getOrderList(int storeId) {
        List<Order> orderList = orderRepository.findByStoreId(storeId)
                .orElseThrow(() -> new Exception404("주문 내역이 없습니다."));

        return new StoreResponse.OrderListDTO(orderList);
    }
}
