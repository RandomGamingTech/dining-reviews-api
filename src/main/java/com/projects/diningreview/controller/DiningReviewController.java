package com.projects.diningreview.controller;

import com.projects.diningreview.exceptions.*;
import com.projects.diningreview.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.projects.diningreview.repository.DiningReviewRepository;
import com.projects.diningreview.repository.RestaurantRepository;
import com.projects.diningreview.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class DiningReviewController {
    private final DiningReviewRepository diningReviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public DiningReviewController(final DiningReviewRepository diningReviewRepository, final UserRepository userRepository, final RestaurantRepository restaurantRepository) {
        this.diningReviewRepository = diningReviewRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        System.out.println("*** CONTROLADOR CREADO ***");
    }

    @GetMapping("/test")
    public String test() {
        return "working";
    }

    @PostMapping(path = "/users")
    public User createUser(@RequestBody User user) {
        Optional<User> userToCreateOptional = this.userRepository.findByDisplayName(user.getDisplayName());
        if (userToCreateOptional.isPresent()) {
            throw new UserDuplicateException();
        }

        if (user.getZipCode() == null || !user.getZipCode().matches("^[0-9]{5}$")) {
            throw new InvalidZipCodeException();
        }

        return this.userRepository.save(user);
    }

    @PutMapping(path = "/users/{displayName}")
    public User updateUser(@PathVariable String displayName, @RequestBody User user) {
        Optional<User> userToUpdateOptional = this.userRepository.findByDisplayName(displayName);
        if (!userToUpdateOptional.isPresent()) {
            throw new UserNotFoundException();
        }

        User userToUpdate = userToUpdateOptional.get();
        if (user.getCity() != null) {
            userToUpdate.setCity(user.getCity());
        }
        if (user.getState() != null) {
            userToUpdate.setState(user.getState());
        }

        if (user.getZipCode() != null) {
            if (user.getZipCode().matches("^[0-9]{5}$")) {
                userToUpdate.setZipCode(user.getZipCode());
            } else {
                throw new InvalidZipCodeException();
            }
        }

        if (user.getPeanutAllergy() != null) {
            userToUpdate.setPeanutAllergy(user.getPeanutAllergy());
        }
        if (user.getEggAllergy() != null) {
            userToUpdate.setEggAllergy(user.getEggAllergy());
        }

        if (user.getDairyAllergy() != null) {
            userToUpdate.setDairyAllergy(user.getDairyAllergy());
        }

        return this.userRepository.save(userToUpdate);
    }

    @GetMapping(path = "/users/{displayName}")
    public User getUserByDisplayName(@PathVariable String displayName) {
        Optional<User> userOptional = this.userRepository.findByDisplayName(displayName);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        return user;
    }

    @PostMapping(path = "/restaurants")
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        Optional<Restaurant> restaurantOptional = this.restaurantRepository.findByName(restaurant.getName());
        if (restaurantOptional.isPresent()) {
            throw new RestaurantDuplicateException();
        }

        if (restaurant.getZipCode() == null || !restaurant.getZipCode().matches("^[0-9]{5}$")) {
            throw new InvalidZipCodeException();
        }

        return this.restaurantRepository.save(restaurant);
    }

    @GetMapping(path = "/restaurants/{id}")
    public Restaurant getRestaurantById(@PathVariable Long id) {
        Optional<Restaurant> restaurantOptional = this.restaurantRepository.findById(id);
        if (!restaurantOptional.isPresent()) {
            throw new RestaurantNotFoundException();
        }
        Restaurant restaurant = restaurantOptional.get();
        return restaurant;
    }

    @GetMapping(path = "/restaurants")
    public List<Restaurant> getRestaurantsByZipCodeOrderByAllergy(@RequestParam String zipCode, @RequestParam String allergy) {
        if (!zipCode.matches("^[0-9]{5}$")) {
            throw new InvalidZipCodeException();
        }

        if (allergy != null && !allergy.isBlank()) {
            switch (allergy.toLowerCase()) {
                case "peanut":
                    return this.restaurantRepository.findByZipCodeOrderByPeanutScoreDesc(zipCode);

                case "egg":
                    return this.restaurantRepository.findByZipCodeOrderByEggScoreDesc(zipCode);

                case "dairy":
                    return this.restaurantRepository.findByZipCodeOrderByDairyScoreDesc(zipCode);

                default:
                    throw new InvalidAllergyException();

            }
        } else {
            throw new InvalidAllergyException();
        }
    }


    @PostMapping(path= "/dining-reviews")
    public DiningReview createReview(@RequestBody DiningReview diningReview) {

        Optional<User> userOptional = this.userRepository.findByDisplayName(diningReview.getDisplayName());
        Optional<Restaurant> restaurantOptional = this.restaurantRepository.findById(diningReview.getRestaurantId());

        if (!userOptional.isPresent()) {
            throw new UserNotFoundException();
        }

        if (!restaurantOptional.isPresent()) {
            throw new RestaurantNotFoundException();
        }

        validateScore(diningReview.getEggScore());

        validateScore(diningReview.getDairyScore());

        validateScore(diningReview.getPeanutScore());

        diningReview.setReviewStatus(ReviewStatus.PENDING);
        return this.diningReviewRepository.save(diningReview);

    }

    private void validateScore(Integer score) {
        if (score != null) {
            if (score < 1 || score > 5) {
                throw new InvalidScoreException();
            }
        }
    }

    @GetMapping(path = "/admin/reviews")
    public List<DiningReview> getPendingAdminReviews() {
        return this.diningReviewRepository.findByReviewStatus(ReviewStatus.PENDING);
    }

    @PutMapping(path = "/admin/reviews/{id}")
    public Restaurant setReviewStatusAndOverallScore(@PathVariable Long id, @RequestBody AdminReviewAction adminReviewAction) {
        Optional<DiningReview> diningReviewOptional = this.diningReviewRepository.findById(id);
        if (!diningReviewOptional.isPresent()) {
            throw new DiningReviewNotFoundException();
        }
        DiningReview diningReview = diningReviewOptional.get();

        if (adminReviewAction.getApproved()) {
            diningReview.setReviewStatus(ReviewStatus.APPROVED);
        } else {
            diningReview.setReviewStatus(ReviewStatus.REJECTED);
        }

        this.diningReviewRepository.save(diningReview);

        Optional<Restaurant> restaurantOptional = this.restaurantRepository.findById(diningReview.getRestaurantId());
        if (!restaurantOptional.isPresent()) {
            throw new RestaurantNotFoundException();
        }
        Restaurant restaurant = restaurantOptional.get();
        return updateRestaurantScores(restaurant);

    }

    private Float calculateAllergyScore(Long id, String allergyName) {
        switch (allergyName) {
            case "peanut": {
                List<DiningReview> approvedDiningReviewsPerRestaurant = this.diningReviewRepository.findByRestaurantIdAndReviewStatus(id, ReviewStatus.APPROVED);
                Float peanutScore = 0.0f;
                Integer peanutCounter = 0;
                for (DiningReview review : approvedDiningReviewsPerRestaurant) {
                    if (review.getPeanutScore() != null) {
                        peanutScore += review.getPeanutScore();
                        peanutCounter++;
                    }
                }

                Float peanutScoreAvg = 0.0f;
                if (peanutCounter > 0) {
                    peanutScoreAvg = peanutScore / peanutCounter;
                } else {
                    peanutScoreAvg = null;
                }

                return roundToTwoDecimals(peanutScoreAvg);
            }
            case "egg": {
                List<DiningReview> approvedDiningReviewsPerRestaurant = this.diningReviewRepository.findByRestaurantIdAndReviewStatus(id, ReviewStatus.APPROVED);
                Float eggScore = 0.0f;
                Integer eggCounter = 0;
                for (DiningReview review : approvedDiningReviewsPerRestaurant) {
                    if (review.getEggScore() != null) {
                        eggScore += review.getEggScore();
                        eggCounter++;
                    }
                }
                Float eggScoreAvg = 0.0f;
                if (eggCounter > 0) {
                    eggScoreAvg = eggScore / eggCounter;
                } else {
                    eggScoreAvg = null;
                }
                return roundToTwoDecimals(eggScoreAvg);
            }
            case "dairy": {
                List<DiningReview> approvedDiningReviewsPerRestaurant = this.diningReviewRepository.findByRestaurantIdAndReviewStatus(id, ReviewStatus.APPROVED);
                Float dairyScore = 0.0f;
                Integer dairyCounter = 0;
                for (DiningReview review : approvedDiningReviewsPerRestaurant) {
                    if (review.getDairyScore() != null) {
                        dairyScore += review.getDairyScore();
                        dairyCounter++;
                    }
                }
                Float dairyScoreAvg = 0.0f;
                if (dairyCounter > 0) {
                    dairyScoreAvg = dairyScore / dairyCounter;
                } else {
                    dairyScoreAvg = null;
                }
                return roundToTwoDecimals(dairyScoreAvg);
            }

            default:
                throw new InvalidScoreNameException(allergyName);
        }
    }

    private Float calculateOverallScore(Float peanutScore, Float eggScore, Float dairyScore) {
        List<Float> restaurantScores = Arrays.asList(peanutScore, eggScore, dairyScore);

        Float overallScore = 0.0f;
        Float sum = 0.0f;
        Integer counter = 0;

        for (Float score : restaurantScores) {
            if (score != null) {
                sum += score;
                counter++;
            }
        }
        if (counter > 0) {
            overallScore = sum / counter;
        } else {
            overallScore = null;
        }

        return roundToTwoDecimals(overallScore);
    }

    private Float roundToTwoDecimals(Float floatToRound) {
        if (floatToRound == null) {
            return null;
        } else {
            Float roundNumber = Math.round(floatToRound * 100) / 100.0f;
            return roundNumber;
        }
    }

    private Restaurant updateRestaurantScores(Restaurant restaurantToUpdate) {
        Float newPeanutScore = calculateAllergyScore(restaurantToUpdate.getId(), "peanut");
        Float newEggScore = calculateAllergyScore(restaurantToUpdate.getId(), "egg");
        Float newDairyScore = calculateAllergyScore(restaurantToUpdate.getId(), "dairy");
        Float overallScore =  calculateOverallScore(newPeanutScore, newEggScore, newDairyScore);
        restaurantToUpdate.setPeanutScore(newPeanutScore);
        restaurantToUpdate.setEggScore(newEggScore);
        restaurantToUpdate.setDairyScore(newDairyScore);
        restaurantToUpdate.setOverallScore(overallScore);
        return restaurantRepository.save(restaurantToUpdate);
    }

    @DeleteMapping(path="/admin/reviews/{id}")
    public void deleteReview(@PathVariable Long id) {
        Optional<DiningReview> review = this.diningReviewRepository.findById(id);
        if (!review.isPresent()) {
            throw new DiningReviewNotFoundException();
        }

        DiningReview reviewToDelete = review.get();
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(reviewToDelete.getRestaurantId());
        if (!restaurantOptional.isPresent()) {
            throw new RestaurantNotFoundException();
        }
        Restaurant restaurantToUpdate = restaurantOptional.get();
        this.diningReviewRepository.deleteById(id);
        if(reviewToDelete.getReviewStatus()==ReviewStatus.APPROVED) {
            updateRestaurantScores(restaurantToUpdate);
        }
    }

}
